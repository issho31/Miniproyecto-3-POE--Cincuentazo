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
public abstract class Jugador {

    protected final String nombre;
    protected final List<Carta> mano;
    protected boolean eliminado;

    /**
     * @param nombre el nombre visible del jugador, no puede ser nulo
     * @throws NullPointerException si {@code nombre} es nulo
     */
    protected Jugador(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.mano = new ArrayList<>(4);
        this.eliminado = false;
    }

    /**
     * @return el nombre visible de este jugador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return una vista de solo lectura de la mano actual del jugador.
     */
    public List<Carta> getMano() {
        return Collections.unmodifiableList(mano);
    }

    /**
     * @return la cantidad de cartas que el jugador tiene actualmente en la mano.
     */
    public int cantidadCartas() {
        return mano.size();
    }

    /**
     * @return {@code true} si el jugador ya fue eliminado de la partida.
     */
    public boolean isEliminado() {
        return eliminado;
    }

    public void eliminar() {
        this.eliminado = true;
    }

    /**
         * @param carta la carta a agregar, no puede ser nula
     * @throws NullPointerException si {@code carta} es nula
     */
    public void recibirCarta(Carta carta) {
        Objects.requireNonNull(carta, "La carta no puede ser nula");
        mano.add(carta);
    }

    /**
         * @param carta la carta que se desea jugar
     * @return la misma carta recibida, ya removida de la mano
     * @throws CartaInvalidaException si la carta no está en la mano
     */
    public Carta jugarCarta(Carta carta) {
        if (!mano.remove(carta)) {
            throw new CartaInvalidaException(
                    nombre + " no tiene la carta " + carta + " en su mano.");
        }
        return carta;
    }

    /**
         * @return las cartas que el jugador tenía en la mano antes de vaciarla
     */
    public List<Carta> vaciarMano() {
        List<Carta> cartas = new ArrayList<>(mano);
        mano.clear();
        return cartas;
    }

    /**
     * @param sumaActual la suma vigente en la mesa
     * @return {@code true} si existe al menos una carta en la mano jugable
     *         sin exceder 50 (regla principal)
     */
    public boolean tieneJugadaValida(int sumaActual) {
        for (Carta carta : mano) {
            if (carta.esJugable(sumaActual)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return {@code true} si este jugador es controlado por la máquina.
     */
    public abstract boolean esMaquina();
}
