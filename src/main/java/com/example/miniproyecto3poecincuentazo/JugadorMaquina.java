package com.example.miniproyecto3poecincuentazo;

import java.util.Comparator;
import java.util.List;


/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
public class JugadorMaquina extends Jugador {

    /**
     * @param nombre el nombre visible de este jugador máquina
     */
    public JugadorMaquina(String nombre) {
        super(nombre);
    }

    @Override
    public boolean esMaquina() {
        return true;
    }

    /**
     * @param sumaActual la suma vigente en la mesa
     * @return la carta elegida, o {@code null} si ninguna es jugable
     *         (debería verificarse antes con {@link #tieneJugadaValida(int)})
     */
    public Carta elegirCarta(int sumaActual) {
        List<Carta> manoActual = getMano();
        return manoActual.stream()
                .filter(carta -> carta.esJugable(sumaActual))
                .max(Comparator.comparingInt(carta -> carta.mejorValorPara(sumaActual)))
                .orElse(null);
    }
}
