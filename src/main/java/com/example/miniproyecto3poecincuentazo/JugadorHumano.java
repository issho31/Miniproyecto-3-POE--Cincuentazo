package com.example.miniproyecto3poecincuentazo;


/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
public class JugadorHumano extends Jugador {

    /**
     * @param nombre el nombre elegido por el jugador humano
     */
    public JugadorHumano(String nombre) {
        super(nombre);
    }

    @Override
    public boolean esMaquina() {
        return false;
    }
}
