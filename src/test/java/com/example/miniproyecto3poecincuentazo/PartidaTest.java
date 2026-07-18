package com.example.miniproyecto3poecincuentazo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartidaTest {

    @Test
    @DisplayName("El constructor rechaza una cantidad de máquinas fuera de 1..3")
    void constructorRechazaCantidadDeMaquinasInvalida() {
        assertThrows(NumeroJugadoresInvalidoException.class, () -> new Partida("Jugador", 0));
        assertThrows(NumeroJugadoresInvalidoException.class, () -> new Partida("Jugador", 4));
    }

    @Test
    @DisplayName("El constructor arma un humano en la posición 0 y N máquinas después")
    void constructorArmaLosJugadoresCorrectamente() {
        Partida partida = new Partida("Ana", 2);
        List<Jugador> jugadores = partida.getJugadores();

        assertEquals(3, jugadores.size());
        assertFalse(jugadores.get(0).esMaquina());
        assertEquals("Ana", jugadores.get(0).getNombre());
        assertTrue(jugadores.get(1).esMaquina());
        assertTrue(jugadores.get(2).esMaquina());
    }

    @Test
    @DisplayName("iniciar() reparte 4 cartas a cada jugador y dispone una carta inicial válida en la mesa")
    void iniciarRepartCuatroCartasYSumaInicialValida() {
        Partida partida = new Partida("Ana", 3);
        partida.iniciar();

        for (Jugador jugador : partida.getJugadores()) {
            assertEquals(Partida.CARTAS_POR_JUGADOR, jugador.cantidadCartas());
        }
        assertEquals(1, partida.getMesa().size());
        // La suma inicial solo puede ser 0 (9), 1 (A), -10 (J/Q/K) o el número de la carta (2-8, 10).
        assertTrue(partida.getSumaMesa() <= Partida.SUMA_MAXIMA);
        assertTrue(partida.getSumaMesa() >= -10);
        assertEquals(0, partida.getJugadores().indexOf(partida.getJugadorActual()));
    }

    @Test
    @DisplayName("jugarCartaJugadorActual aplica la carta, actualiza la suma y repone la mano")
    void jugarCartaJugadorActualActualizaEstado() {
        Partida partida = new Partida("Ana", 1);
        partida.iniciar();

        Jugador actual = partida.getJugadorActual();
        int sumaAntes = partida.getSumaMesa();
        Carta cartaJugable = actual.getMano().stream()
                .filter(c -> c.esJugable(sumaAntes))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Se esperaba al menos una carta jugable"));
        int valorAplicado = cartaJugable.mejorValorPara(sumaAntes);

        partida.jugarCartaJugadorActual(cartaJugable, null);

        assertEquals(sumaAntes + valorAplicado, partida.getSumaMesa());
        assertEquals(Partida.CARTAS_POR_JUGADOR, actual.cantidadCartas()); // volvió a tomar una carta
        assertFalse(actual.getMano().contains(cartaJugable)); // la que jugó ya no está (salvo que haya tomado otra igual, poco probable)
        assertTrue(partida.getMesa().contains(cartaJugable));
    }

    @Test
    @DisplayName("jugarCartaJugadorActual lanza CartaInvalidaException si la carta no está en la mano")
    void jugarCartaQueNoEstaEnLaManoLanzaExcepcion() {
        Partida partida = new Partida("Ana", 1);
        partida.iniciar();

        Carta cartaAjena = new Carta(Palo.PICAS, Valor.REY);
        while (partida.getJugadorActual().getMano().contains(cartaAjena)) {
            // En el remotísimo caso de que justo la tenga, probamos con otra.
            cartaAjena = new Carta(Palo.CORAZONES, Valor.DOS);
        }

        Carta cartaFinal = cartaAjena;
        assertThrows(CartaInvalidaException.class,
                () -> partida.jugarCartaJugadorActual(cartaFinal, null));
    }

    @Test
    @DisplayName("verificarYEliminarSiNoPuedeJugar no elimina a un jugador que sí tiene jugada válida")
    void noEliminaAJugadorConJugadaValida() {
        Partida partida = new Partida("Ana", 1);
        partida.iniciar();

        Jugador actual = partida.getJugadorActual();
        boolean fueEliminado = partida.verificarYEliminarSiNoPuedeJugar();

        assertFalse(fueEliminado);
        assertFalse(actual.isEliminado());
    }

    @Test
    @DisplayName("Una partida completa siempre termina con exactamente un ganador y la suma nunca supera 50")
    void unaPartidaCompletaTerminaConUnGanador() {
        for (int cantidadMaquinas = 1; cantidadMaquinas <= 3; cantidadMaquinas++) {
            Partida partida = new Partida("Ana", cantidadMaquinas);
            partida.iniciar();

            int limiteDeSeguridad = 0;
            while (!partida.isTerminada() && limiteDeSeguridad < 5000) {
                limiteDeSeguridad++;
                if (partida.verificarYEliminarSiNoPuedeJugar()) {
                    continue;
                }
                Jugador actual = partida.getJugadorActual();
                int sumaActual = partida.getSumaMesa();
                Carta elegida = actual.getMano().stream()
                        .filter(c -> c.esJugable(sumaActual))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No debería llegar aquí sin jugada válida"));
                partida.jugarCartaJugadorActual(elegida, null);
                assertTrue(partida.getSumaMesa() <= Partida.SUMA_MAXIMA);
            }

            assertTrue(partida.isTerminada(), "La partida debía terminar dentro del límite de seguridad");
            long activos = partida.getJugadores().stream().filter(j -> !j.isEliminado()).count();
            assertEquals(1, activos);
            assertNotNull(partida.getGanador());
        }
    }
}
