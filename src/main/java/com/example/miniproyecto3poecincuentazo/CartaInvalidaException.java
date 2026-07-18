package com.example.miniproyecto3poecincuentazo;

/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
public class CartaInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje descripción de por qué la carta o el valor elegido no es válido
     */
    public CartaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
