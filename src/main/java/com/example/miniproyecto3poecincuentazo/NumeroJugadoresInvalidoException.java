package com.example.miniproyecto3poecincuentazo;

public class NumeroJugadoresInvalidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NumeroJugadoresInvalidoException(String mensaje) {
        super(mensaje);
    }
}
