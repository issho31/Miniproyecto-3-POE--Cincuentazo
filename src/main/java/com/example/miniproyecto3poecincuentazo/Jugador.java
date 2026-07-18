package com.example.miniproyecto3poecincuentazo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Jugador {

    protected final String nombre;
    protected final List<Carta> mano;
    protected boolean eliminado;

    protected Jugador(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.mano = new ArrayList<>(4);
        this.eliminado = false;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Carta> getMano() {
        return Collections.unmodifiableList(mano);
    }

    public int cantidadCartas() {
        return mano.size();
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void eliminar() {
        this.eliminado = true;
    }

    public void recibirCarta(Carta carta) {
        Objects.requireNonNull(carta, "La carta no puede ser nula");
        mano.add(carta);
    }

    public Carta jugarCarta(Carta carta) {
        if (!mano.remove(carta)) {
            throw new CartaInvalidaException(
                    nombre + " no tiene la carta " + carta + " en su mano.");
        }
        return carta;
    }

    public List<Carta> vaciarMano() {
        List<Carta> cartas = new ArrayList<>(mano);
        mano.clear();
        return cartas;
    }

    public boolean tieneJugadaValida(int sumaActual) {
        for (Carta carta : mano) {
            if (carta.esJugable(sumaActual)) {
                return true;
            }
        }
        return false;
    }

    public abstract boolean esMaquina();
}
