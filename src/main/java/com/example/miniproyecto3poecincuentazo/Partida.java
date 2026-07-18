package com.example.miniproyecto3poecincuentazo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
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


    /**
     * @param nombreHumano     nombre del jugador humano
     * @param cantidadMaquinas cantidad de jugadores máquina: 1, 2 o 3 (HU-1)
     * @throws NumeroJugadoresInvalidoException si {@code cantidadMaquinas} no está en [1, 3]
     * @throws NullPointerException si {@code nombreHumano} es nulo
     */
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

    /**
     * @return el jugador al que le corresponde jugar en este momento.
     */
    public Jugador getJugadorActual() {
        return jugadores.get(indiceTurno);
    }

    /**
     * @return la suma vigente en la mesa (siempre &le; {@link #SUMA_MAXIMA}).
     */
    public int getSumaMesa() {
        return sumaMesa;
    }

    /**
     * @return una vista de solo lectura de las cartas jugadas en la mesa,
     *         en orden de aparición (la última es la visible/actual).
     */
    public List<Carta> getMesa() {
        return Collections.unmodifiableList(mesa);
    }

    /**
     * @return una vista de solo lectura de todos los jugadores de la
     *         partida (el humano siempre en la posición 0).
     */
    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    /**
     * @return {@code true} si la partida ya terminó (solo queda un jugador activo).
     */
    public boolean isTerminada() {
        return terminada;
    }

    /**
     * @return la cantidad de cartas que quedan disponibles en el mazo.
     */
    public int getCartasEnMazo() {
        return mazo.cartasDisponibles();
    }


    /**
     * @return el jugador ganador una vez terminada la partida, o
     *         {@code null} si todavía está en curso.
     */
    public Jugador getGanador() {
        if (!terminada) {
            return null;
        }
        return jugadores.stream().filter(j -> !j.isEliminado()).findFirst().orElse(null);
    }


    /**
     *
     * @return {@code true} si el jugador actual fue eliminado en esta llamada
     */
    public boolean verificarYEliminarSiNoPuedeJugar() {
        Jugador actual = getJugadorActual();
        if (actual.tieneJugadaValida(sumaMesa)) {
            return false;
        }
        eliminarJugador(actual);
        return true;
    }

    /**
     * @param carta          carta de la mano del jugador actual que se desea jugar
     * @param valorPreferido para el As: 1 o 10, según lo que convenga al
     *                       jugador. Se puede pasar {@code null} para que se
     *                       elija automáticamente el mejor valor válido.
     * @throws CartaInvalidaException si la carta no está en la mano del
     *         jugador actual, si el valor preferido indicado no es válido
     *         con la suma actual, o si ninguno de los valores posibles de
     *         la carta respeta la regla principal (suma &le; 50)
     */
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

    /**
     * @throws IllegalStateException en el caso extremo (no alcanzable en
     *         una partida normal) de que no haya cartas ni en el mazo ni
     *         en la mesa para reciclar
     */
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
