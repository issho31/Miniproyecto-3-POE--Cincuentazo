package com.example.miniproyecto3poecincuentazo;


public class JugadorHumano extends Jugador {

    public JugadorHumano(String nombre) {
        super(nombre);
    }

    @Override
    public boolean esMaquina() {
        return false;
    }
}
