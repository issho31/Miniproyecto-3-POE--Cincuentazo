package com.example.miniproyecto3poecincuentazo;

public enum Valor {
    AS("A", new int[]{1, 10}),
    DOS("2", new int[]{2}),
    TRES("3", new int[]{3}),
    CUATRO("4", new int[]{4}),
    CINCO("5", new int[]{5}),
    SEIS("6", new int[]{6}),
    SIETE("7", new int[]{7}),
    OCHO("8", new int[]{8}),
    NUEVE("9", new int[]{0}),
    DIEZ("10", new int[]{10}),
    JOTA("J", new int[]{-10}),
    REINA("Q", new int[]{-10}),
    REY("K", new int[]{-10});

    private final String etiqueta;
    private final int[] valoresPosibles;

    Valor(String etiqueta, int[] valoresPosibles) {
        this.etiqueta = etiqueta;
        this.valoresPosibles = valoresPosibles;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public int[] getValoresPosibles() {
        return valoresPosibles.clone();
    }

    public boolean tieneValorAmbiguo() {
        return valoresPosibles.length > 1;
    }
}
