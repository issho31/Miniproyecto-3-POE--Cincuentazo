package com.example.miniproyecto3poecincuentazo;

public class CartaInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CartaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
