package com.example.miniproyecto3poecincuentazo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CartaTest {

    @Test
    @DisplayName("El constructor rechaza palo o valor nulos")
    void constructorRechazaNulos() {
        assertThrows(NullPointerException.class, () -> new Carta(null, Valor.AS));
        assertThrows(NullPointerException.class, () -> new Carta(Palo.PICAS, null));
    }

    @Test
    @DisplayName("Las cartas 2 al 8 y el 10 suman su propio número")
    void cartasNumericasSumanSuNumero() {
        Carta siete = new Carta(Palo.DIAMANTES, Valor.SIETE);
        Carta diez = new Carta(Palo.TREBOLES, Valor.DIEZ);

        assertEquals(7, siete.mejorValorPara(0));
        assertEquals(10, diez.mejorValorPara(0));
        assertTrue(siete.esJugable(43));   // 43 + 7 = 50, límite exacto permitido
        assertFalse(siete.esJugable(44));  // 44 + 7 = 51, excede la regla principal
    }

    @Test
    @DisplayName("El 9 no suma ni resta a la mesa")
    void elNueveNoSumaNiResta() {
        Carta nueve = new Carta(Palo.PICAS, Valor.NUEVE);
        assertEquals(0, nueve.mejorValorPara(25));
        assertTrue(nueve.esJugable(50)); // 50 + 0 = 50 sigue siendo válido
    }

    @Test
    @DisplayName("J, Q y K siempre restan 10 a la mesa")
    void figurasRestanDiez() {
        Carta jota = new Carta(Palo.CORAZONES, Valor.JOTA);
        Carta reina = new Carta(Palo.DIAMANTES, Valor.REINA);
        Carta rey = new Carta(Palo.TREBOLES, Valor.REY);

        assertEquals(-10, jota.mejorValorPara(5));
        assertEquals(-10, reina.mejorValorPara(5));
        assertEquals(-10, rey.mejorValorPara(5));
        assertTrue(rey.esJugable(0)); // incluso con la mesa en 0 sigue siendo jugable
    }

    @Test
    @DisplayName("El As suma 1 o 10 según lo que convenga sin exceder 50")
    void elAsEligeElValorMasConveniente() {
        Carta as = new Carta(Palo.CORAZONES, Valor.AS);

        // Con la mesa en 30, ambos valores (31 y 40) son válidos: se prefiere el 10.
        assertEquals(10, as.mejorValorPara(30));

        // Con la mesa en 45, solo el 1 es válido (45+10=55 excede 50).
        assertEquals(1, as.mejorValorPara(45));
        assertTrue(as.esJugable(45));

        // Con la mesa en 50, ni siquiera sumar 1 es válido.
        assertFalse(as.esJugable(50));
    }

    @Test
    @DisplayName("equals y hashCode dependen solo de palo y valor")
    void equalsYHashCodeConsistentes() {
        Carta primeraReferencia = new Carta(Palo.PICAS, Valor.REY);
        Carta mismaCartaOtraInstancia = new Carta(Palo.PICAS, Valor.REY);
        Carta cartaDistinta = new Carta(Palo.PICAS, Valor.REINA);

        assertEquals(primeraReferencia, mismaCartaOtraInstancia);
        assertEquals(primeraReferencia.hashCode(), mismaCartaOtraInstancia.hashCode());
        assertFalse(primeraReferencia.equals(cartaDistinta));
    }

    @Test
    @DisplayName("toString muestra la etiqueta del valor seguida del símbolo del palo")
    void toStringTieneElFormatoEsperado() {
        Carta carta = new Carta(Palo.CORAZONES, Valor.REY);
        assertEquals("K♥", carta.toString());
    }
}
