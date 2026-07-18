package com.example.miniproyecto3poecincuentazo;

import java.util.Comparator;
import java.util.List;


public class JugadorMaquina extends Jugador {

    public JugadorMaquina(String nombre) {
        super(nombre);
    }

    @Override
    public boolean esMaquina() {
        return true;
    }

    public Carta elegirCarta(int sumaActual) {
        List<Carta> manoActual = getMano();
        return manoActual.stream()
                .filter(carta -> carta.esJugable(sumaActual))
                .max(Comparator.comparingInt(carta -> carta.mejorValorPara(sumaActual)))
                .orElse(null);
    }
}
