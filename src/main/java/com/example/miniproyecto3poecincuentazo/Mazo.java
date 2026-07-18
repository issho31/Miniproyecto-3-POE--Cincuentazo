package com.example.miniproyecto3poecincuentazo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;


/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
public class Mazo {

    private final Deque<Carta> cartas;

    public Mazo() {
        this.cartas = new ArrayDeque<>();
        generarBaraja();
        barajar();
    }

    private void generarBaraja() {
        cartas.clear();
        for (Palo palo : Palo.values()) {
            for (Valor valor : Valor.values()) {
                cartas.push(new Carta(palo, valor));
            }
        }
    }

    public void barajar() {
        List<Carta> temp = new ArrayList<>(cartas);
        Collections.shuffle(temp);
        cartas.clear();
        cartas.addAll(temp);
    }

    /**
     * @return la cantidad de cartas disponibles actualmente en el mazo.
     */
    public int cartasDisponibles() {
        return cartas.size();
    }

    /**
     * @return {@code true} si el mazo no tiene cartas para entregar.
     */
    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    /**
         * @return la carta tomada
     * @throws NoHayCartasDisponiblesException si el mazo está vacío. Quien
     *         orquesta la partida ({@link Partida}) debe intentar
     *         {@link #reciclar(List, Carta)} antes de propagar este error.
     */
    public Carta tomarCarta() throws NoHayCartasDisponiblesException {
        if (cartas.isEmpty()) {
            throw new NoHayCartasDisponiblesException(
                    "No quedan cartas disponibles en el mazo.");
        }
        return cartas.pop();
    }

    /**
     * Recicla las cartas jugadas en la mesa cuando el mazo se queda sin
     * cartas: todas vuelven al mazo (barajadas) excepto la última jugada,
     * que permanece visible en la mesa. La suma de la mesa no se modifica
     * por esta operación.
     *
     * @param cartasMesa   las cartas actualmente apiladas en la mesa
     * @param ultimaJugada la carta que debe permanecer en la mesa (no se recicla)
     */
    public void reciclar(List<Carta> cartasMesa, Carta ultimaJugada) {
        Objects.requireNonNull(cartasMesa, "La lista de cartas de la mesa no puede ser nula");
        List<Carta> reciclables = new ArrayList<>(cartasMesa);
        reciclables.remove(ultimaJugada);
        Collections.shuffle(reciclables);
        cartas.addAll(reciclables);
    }

    /**
     *
     * @param cartasJugador las cartas en la mano del jugador eliminado
     */
    public void enviarAlFinal(List<Carta> cartasJugador) {
        Objects.requireNonNull(cartasJugador, "Las cartas del jugador no pueden ser nulas");
        for (Carta carta : cartasJugador) {
            cartas.addLast(carta);
        }
    }
}
