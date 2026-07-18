package com.example.miniproyecto3poecincuentazo;

import com.cincuentazo.model.NumeroJugadoresInvalidoException;
import com.cincuentazo.model.Partida;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class ConfiguracionController {

    private static final String VISTA_JUEGO = "/com/cincuentazo/view/juego.fxml";
    private static final String NOMBRE_POR_DEFECTO = "Jugador";

    @FXML private TextField campoNombreJugador;
    @FXML private ToggleGroup grupoCantidadMaquinas;
    @FXML private RadioButton radioUnaMaquina;
    @FXML private RadioButton radioDosMaquinas;
    @FXML private RadioButton radioTresMaquinas;
    @FXML private Label etiquetaError;

    @FXML
    private void initialize() {
        etiquetaError.setText("");
        radioUnaMaquina.setSelected(true);
    }

    @FXML
    private void manejarIniciarJuego() {
        etiquetaError.setText("");
        int cantidadMaquinas = obtenerCantidadMaquinasSeleccionada();
        String nombreJugador = obtenerNombreJugador();

        try {
            Partida partida = new Partida(nombreJugador, cantidadMaquinas);
            partida.iniciar();

            JuegoController controlador = SceneNavigator.cambiarEscena(VISTA_JUEGO, "Cincuentazo");
            controlador.iniciarPartida(partida);
        } catch (NumeroJugadoresInvalidoException e) {
            etiquetaError.setText(e.getMessage());
        }
    }

    private int obtenerCantidadMaquinasSeleccionada() {
        Toggle seleccionado = grupoCantidadMaquinas.getSelectedToggle();
        if (seleccionado == radioDosMaquinas) {
            return 2;
        }
        if (seleccionado == radioTresMaquinas) {
            return 3;
        }
        return 1;
    }

    private String obtenerNombreJugador() {
        String nombre = campoNombreJugador.getText();
        if (nombre == null || nombre.isBlank()) {
            return NOMBRE_POR_DEFECTO;
        }
        return nombre.trim();
    }
}
