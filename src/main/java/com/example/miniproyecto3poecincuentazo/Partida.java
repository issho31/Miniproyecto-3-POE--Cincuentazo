package com.example.miniproyecto3poecincuentazo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Partida {


    public static final int SUMA_MAXIMA = 50;
    public static final int CARTAS_POR_JUGADOR = 4;

    private static final int MIN_MAQUINAS = 1;
    private static final int MAX_MAQUINAS = 3;

    private final Mazo mazo;
    private final List<Jugador> jugadores;
    private final List<Carta> mesa;
    private int sumaMesa;
    private int indiceTurno;
    private boolean terminada;


    public Partida(String nombreHumano, int cantidadMaquinas) {
        Objects.requireNonNull(nombreHumano, "El nombre del jugador humano no puede ser nulo");
        if (cantidadMaquinas < MIN_MAQUINAS || cantidadMaquinas > MAX_MAQUINAS) {
            throw new NumeroJugadoresInvalidoException(
                    "La cantidad de jugadores máquina debe ser 1, 2 o 3 (recibido: " + cantidadMaquinas + ").");
        }
        this.mazo = new Mazo();
        this.mesa = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.jugadores.add(new JugadorHumano(nombreHumano));
        for (int i = 1; i <= cantidadMaquinas; i++) {
            this.jugadores.add(new JugadorMaquina("Máquina " + i));
        }
        this.indiceTurno = 0;
        this.terminada = false;
        this.sumaMesa = 0;
    }

    public void iniciar() {
        for (int ronda = 0; ronda < CARTAS_POR_JUGADOR; ronda++) {
            for (Jugador jugador : jugadores) {
                jugador.recibirCarta(tomarCartaConReciclaje());
            }
        }
        Carta cartaInicial = tomarCartaConReciclaje();
        mesa.add(cartaInicial);
        // La carta inicial no se "juega": se usa su valor base (el primero de
        // los posibles), que coincide con la regla del enunciado (9->0, A->1, J/Q/K->-10).
        sumaMesa = cartaInicial.getValoresPosibles()[0];
        indiceTurno = 0;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(indiceTurno);
    }

    public int getSumaMesa() {
        return sumaMesa;
    }

    public List<Carta> getMesa() {
        return Collections.unmodifiableList(mesa);
    }

    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    public boolean isTerminada() {
        return terminada;
    }

    public int getCartasEnMazo() {
        return mazo.cartasDisponibles();
    }


    public Jugador getGanador() {
        if (!terminada) {
            return null;
        }
        return jugadores.stream().filter(j -> !j.isEliminado()).findFirst().orElse(null);
    }


    public boolean verificarYEliminarSiNoPuedeJugar() {
        Jugador actual = getJugadorActual();
        if (actual.tieneJugadaValida(sumaMesa)) {
            return false;
        }
        eliminarJugador(actual);
        return true;
    }

    public void jugarCartaJugadorActual(Carta carta, Integer valorPreferido) {
        Jugador actual = getJugadorActual();
        if (!actual.getMano().contains(carta)) {
            throw new CartaInvalidaException(
                    actual.getNombre() + " no tiene esa carta en su mano.");
        }

        int valorAplicado = resolverValorAplicado(carta, valorPreferido);

        actual.jugarCarta(carta);
        mesa.add(carta);
        sumaMesa += valorAplicado;

        actual.recibirCarta(tomarCartaConReciclaje());

        verificarFinDeJuego();
        if (!terminada) {
            avanzarTurno();
        }
    }

    private int resolverValorAplicado(Carta carta, Integer valorPreferido) {
        int[] posibles = carta.getValoresPosibles();
        if (valorPreferido != null) {
            for (int posible : posibles) {
                if (posible == valorPreferido && sumaMesa + posible <= SUMA_MAXIMA) {
                    return posible;
                }
            }
            throw new CartaInvalidaException(
                    "El valor " + valorPreferido + " no es válido para " + carta
                            + " con la suma actual (" + sumaMesa + ").");
        }
        if (!carta.esJugable(sumaMesa)) {
            throw new CartaInvalidaException(
                    "La carta " + carta + " excede la suma máxima de 50 (suma actual: " + sumaMesa + ").");
        }
        return carta.mejorValorPara(sumaMesa);
    }

    private void eliminarJugador(Jugador jugador) {
        jugador.eliminar();
        List<Carta> cartasDevueltas = jugador.vaciarMano();
        mazo.enviarAlFinal(cartasDevueltas);
        verificarFinDeJuego();
        if (!terminada) {
            avanzarTurno();
        }
    }

    private void verificarFinDeJuego() {
        long activos = jugadores.stream().filter(j -> !j.isEliminado()).count();
        if (activos <= 1) {
            terminada = true;
        }
    }

    private void avanzarTurno() {
        int total = jugadores.size();
        int intentos = 0;
        do {
            indiceTurno = (indiceTurno + 1) % total;
            intentos++;
        } while (jugadores.get(indiceTurno).isEliminado() && intentos <= total);
    }

    private Carta tomarCartaConReciclaje() {
        try {
            return mazo.tomarCarta();
        } catch (NoHayCartasDisponiblesException agotado) {
            if (mesa.size() <= 1) {
                throw new IllegalStateException(
                        "No hay cartas disponibles ni en el mazo ni en la mesa para reciclar.", agotado);
            }
            Carta ultima = mesa.get(mesa.size() - 1);
            mazo.reciclar(mesa, ultima);
            mesa.clear();
            mesa.add(ultima);
            try {
                return mazo.tomarCarta();
            } catch (NoHayCartasDisponiblesException imposible) {
                throw new IllegalStateException("Error inesperado reciclando el mazo.", imposible);
            }
        }
    }
}
