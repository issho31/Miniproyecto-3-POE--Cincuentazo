package com.example.miniproyecto3poecincuentazo;

/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
public class NoHayCartasDisponiblesException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje descripción del motivo por el cual no hay cartas disponibles
     */
    public NoHayCartasDisponiblesException(String mensaje) {
        super(mensaje);
    }

    /**
     * @param mensaje descripción del motivo por el cual no hay cartas disponibles
     * @param causa   la excepción original que originó este error, si aplica
     */
    public NoHayCartasDisponiblesException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
