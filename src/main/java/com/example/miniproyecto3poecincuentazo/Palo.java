package com.example.miniproyecto3poecincuentazo;


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

    public String getNombre() {
        return nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
