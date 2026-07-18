package com.example.miniproyecto3poecincuentazo;

/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
public class NumeroJugadoresInvalidoException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje descripción de por qué la cantidad de jugadores máquina no es válida
     */
    public NumeroJugadoresInvalidoException(String mensaje) {
        super(mensaje);
    }
}
