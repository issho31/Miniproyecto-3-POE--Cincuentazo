package com.example.miniproyecto3poecincuentazo;

/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
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

    /**
     * @return el texto corto que identifica al valor (p. ej. {@code "A"}, {@code "10"}, {@code "K"}).
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * @return una copia del arreglo de puntos que esta carta puede aportar
     *         a la suma de la mesa (más de un elemento solo en el caso del As).
     *         Se devuelve una copia para que el arreglo interno del enum
     *         permanezca inmutable.
     */
    public int[] getValoresPosibles() {
        return valoresPosibles.clone();
    }

    /**
     * @return {@code true} si la carta tiene más de un valor posible
     *         (únicamente el As); {@code false} para el resto de valores.
     */
    public boolean tieneValorAmbiguo() {
        return valoresPosibles.length > 1;
    }
}
