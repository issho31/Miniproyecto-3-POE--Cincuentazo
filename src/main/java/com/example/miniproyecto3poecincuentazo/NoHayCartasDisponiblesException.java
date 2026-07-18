package com.example.miniproyecto3poecincuentazo;

public class NoHayCartasDisponiblesException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoHayCartasDisponiblesException(String mensaje) {
        super(mensaje);
    }

    public NoHayCartasDisponiblesException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
