package com.example.miniproyecto3poecincuentazo;

import java.util.Objects;

/**
 * Representa una carta de la baraja, definida por su {@link Palo} y su {@link Valor}.
 * Es inmutable: una vez creada, ni el palo ni el valor pueden cambiar.
 */
public class Carta {

    private final Palo palo;
    private final Valor valor;

    public Carta(Palo palo, Valor valor) {
        this.palo = Objects.requireNonNull(palo, "El palo no puede ser nulo");
        this.valor = Objects.requireNonNull(valor, "El valor no puede ser nulo");
    }

    public Palo getPalo() {
        return palo;
    }

    public Valor getValor() {
        return valor;
    }

    /**
     * @return los puntos que esta carta puede aportar a la suma de la mesa.
     *         Solo el As devuelve más de un valor posible ({1, 10}).
     */
    public int[] getValoresPosibles() {
        return valor.getValoresPosibles();
    }

    /**
     * Determina si la carta puede jugarse sin exceder la regla principal (suma &le; 50),
     * probando todos sus valores posibles (relevante para el As).
     *
     * @param sumaActual la suma vigente en la mesa antes de jugar esta carta
     * @return true si existe al menos un valor posible que no haga exceder 50
     */
    public boolean esJugable(int sumaActual) {
        for (int posible : getValoresPosibles()) {
            if (sumaActual + posible <= 50) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calcula el mejor valor a aplicar dado el estado actual de la mesa: el que
     * deje la suma más alta posible sin superar 50. Útil para la IA de los
     * jugadores máquina y como valor por defecto sugerido al jugador humano
     * cuando juega un As.
     *
     * @param sumaActual la suma vigente en la mesa antes de jugar esta carta
     * @return el valor a aplicar; si ningún valor es válido, retorna el menor
     *         de los posibles (para que el llamador pueda reportar el excedente)
     */
    public int mejorValorPara(int sumaActual) {
        boolean encontrado = false;
        int mejor = Integer.MIN_VALUE;
        int menor = Integer.MAX_VALUE;

        for (int posible : getValoresPosibles()) {
            menor = Math.min(menor, posible);
            if (sumaActual + posible <= 50 && posible > mejor) {
                mejor = posible;
                encontrado = true;
            }
        }
        return encontrado ? mejor : menor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carta)) return false;
        Carta carta = (Carta) o;
        return palo == carta.palo && valor == carta.valor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(palo, valor);
    }

    @Override
    public String toString() {
        return valor.getEtiqueta() + palo.getSimbolo();
    }
}
