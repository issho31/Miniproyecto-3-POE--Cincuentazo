package com.example.miniproyecto3poecincuentazo;


/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
public enum Palo {
    CORAZONES("Corazones", "♥"),
    DIAMANTES("Diamantes", "♦"),
    TREBOLES("Tréboles", "♣"),
    PICAS("Picas", "♠");

    private final String nombre;
    private final String simbolo;

    Palo(String nombre, String simbolo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
    }

    /**
     * @return el nombre completo del palo en español (p. ej. {@code "Corazones"}).
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return el símbolo Unicode del palo (p. ej. {@code "♥"}), usado para
     *         mostrar la carta de forma compacta en la interfaz.
     */
    public String getSimbolo() {
        return simbolo;
    }
}
