package com.example.miniproyecto3poecincuentazo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MazoTest {

    private Mazo mazo;

    @BeforeEach
    void crearMazoNuevo() {
        mazo = new Mazo();
    }

    @Test
    @DisplayName("Un mazo nuevo tiene exactamente 52 cartas y no está vacío")
    void mazoNuevoTiene52Cartas() {
        assertEquals(52, mazo.cartasDisponibles());
        assertFalse(mazo.estaVacio());
    }

    @Test
    @DisplayName("Tomar una carta reduce en uno las cartas disponibles")
    void tomarCartaReduceElTamano() throws NoHayCartasDisponiblesException {
        int antes = mazo.cartasDisponibles();
        Carta carta = mazo.tomarCarta();

        assertNotNull(carta);
        assertEquals(antes - 1, mazo.cartasDisponibles());
    }

    @Test
    @DisplayName("Barajar no cambia la cantidad de cartas del mazo")
    void barajarNoPierdeCartas() {
        mazo.barajar();
        assertEquals(52, mazo.cartasDisponibles());
    }

    @Test
    @DisplayName("Tomar una carta de un mazo vacío lanza NoHayCartasDisponiblesException")
    void tomarCartaDeMazoVacioLanzaExcepcion() throws NoHayCartasDisponiblesException {
        for (int i = 0; i < 52; i++) {
            mazo.tomarCarta();
        }
        assertTrue(mazo.estaVacio());
        assertThrows(NoHayCartasDisponiblesException.class, () -> mazo.tomarCarta());
    }

    @Test
    @DisplayName("Reciclar devuelve al mazo todas las cartas de la mesa menos la última jugada")
    void reciclarDevuelveTodoMenosLaUltimaJugada() throws NoHayCartasDisponiblesException {
        // Vaciamos el mazo para simular la situación real en la que se recicla.
        List<Carta> jugadasEnMesa = new ArrayList<>();
        while (!mazo.estaVacio()) {
            jugadasEnMesa.add(mazo.tomarCarta());
        }
        assertTrue(mazo.estaVacio());

        Carta ultimaJugada = jugadasEnMesa.get(jugadasEnMesa.size() - 1);
        mazo.reciclar(jugadasEnMesa, ultimaJugada);

        // Las 52 cartas menos la última jugada (que se queda en la mesa) vuelven al mazo.
        assertEquals(51, mazo.cartasDisponibles());
    }

    @Test
    @DisplayName("Las cartas de un jugador eliminado quedan disponibles al final del mazo")
    void enviarAlFinalDejaLasCartasDisponibles() throws NoHayCartasDisponiblesException {
        Carta cartaUno = mazo.tomarCarta();
        Carta cartaDos = mazo.tomarCarta();
        int disponiblesAntes = mazo.cartasDisponibles();

        mazo.enviarAlFinal(List.of(cartaUno, cartaDos));

        assertEquals(disponiblesAntes + 2, mazo.cartasDisponibles());

        // Deben quedar disponibles para tomarse: si se vacía el resto del mazo,
        // las dos últimas cartas entregadas deben ser justamente esas.
        List<Carta> restantes = new ArrayList<>();
        while (!mazo.estaVacio()) {
            restantes.add(mazo.tomarCarta());
        }
        List<Carta> ultimasDos = restantes.subList(restantes.size() - 2, restantes.size());
        assertTrue(ultimasDos.containsAll(List.of(cartaUno, cartaDos)));
    }
}
