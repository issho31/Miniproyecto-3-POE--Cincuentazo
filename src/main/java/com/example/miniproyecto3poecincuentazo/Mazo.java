package com.example.miniproyecto3poecincuentazo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;


public class Mazo {

    private final Deque<Carta> cartas;

    public Mazo() {
        this.cartas = new ArrayDeque<>();
        generarBaraja();
        barajar();
    }

    private void generarBaraja() {
        cartas.clear();
        for (Palo palo : Palo.values()) {
            for (Valor valor : Valor.values()) {
                cartas.push(new Carta(palo, valor));
            }
        }
    }

    public void barajar() {
        List<Carta> temp = new ArrayList<>(cartas);
        Collections.shuffle(temp);
        cartas.clear();
        cartas.addAll(temp);
    }

    public int cartasDisponibles() {
        return cartas.size();
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }


    public Carta tomarCarta() throws NoHayCartasDisponiblesException {
        if (cartas.isEmpty()) {
            throw new NoHayCartasDisponiblesException(
                    "No quedan cartas disponibles en el mazo.");
        }
        return cartas.pop();
    }

    public void reciclar(List<Carta> cartasMesa, Carta ultimaJugada) {
        Objects.requireNonNull(cartasMesa, "La lista de cartas de la mesa no puede ser nula");
        List<Carta> reciclables = new ArrayList<>(cartasMesa);
        reciclables.remove(ultimaJugada);
        Collections.shuffle(reciclables);
        cartas.addAll(reciclables);
    }

    public void enviarAlFinal(List<Carta> cartasJugador) {
        Objects.requireNonNull(cartasJugador, "Las cartas del jugador no pueden ser nulas");
        for (Carta carta : cartasJugador) {
            cartas.addLast(carta);
        }
    }
}
