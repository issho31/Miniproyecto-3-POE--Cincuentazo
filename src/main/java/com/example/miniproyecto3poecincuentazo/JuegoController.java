package com.example.miniproyecto3poecincuentazo;

import com.cincuentazo.model.Carta;
import com.cincuentazo.model.Jugador;
import com.cincuentazo.model.Partida;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class JuegoController {

    @FXML private Label labelSumaMesa;
    @FXML private Label labelCartasMazo;
    @FXML private Label labelTurno;
    @FXML private Label labelTiempo;
    @FXML private HBox hboxCartaMesa;
    @FXML private HBox hboxManoHumano;
    @FXML private VBox vboxMaquinas;
    @FXML private Button botonSalir;

    private Partida partida;
    private final List<Label> etiquetasMaquinas = new ArrayList<>();

    public void iniciarPartida(Partida partidaIniciada) {
        this.partida = Objects.requireNonNull(partidaIniciada, "La partida no puede ser nula");
        construirEtiquetasMaquinas();
        actualizarVista();
    }

    private void construirEtiquetasMaquinas() {
        vboxMaquinas.getChildren().clear();
        etiquetasMaquinas.clear();
        for (Jugador jugador : partida.getJugadores()) {
            if (jugador.esMaquina()) {
                Label etiqueta = new Label();
                etiquetasMaquinas.add(etiqueta);
                vboxMaquinas.getChildren().add(etiqueta);
            }
        }
    }


    void actualizarVista() {
        labelSumaMesa.setText("Suma en mesa: " + partida.getSumaMesa() + " / " + Partida.SUMA_MAXIMA);
        labelCartasMazo.setText("Cartas en el mazo: " + partida.getCartasEnMazo());
        labelTurno.setText(partida.isTerminada()
                ? "Juego terminado"
                : "Turno de: " + partida.getJugadorActual().getNombre());

        actualizarCartaEnMesa();
        actualizarManoHumano();
        actualizarEtiquetasMaquinas();
    }

    private void actualizarCartaEnMesa() {
        hboxCartaMesa.getChildren().clear();
        List<Carta> mesa = partida.getMesa();
        if (!mesa.isEmpty()) {
            Carta ultima = mesa.get(mesa.size() - 1);
            Label etiquetaCarta = new Label(ultima.toString());
            etiquetaCarta.getStyleClass().add("carta");
            hboxCartaMesa.getChildren().add(etiquetaCarta);
        }
    }

    private void actualizarManoHumano() {
        hboxManoHumano.getChildren().clear();
        Jugador humano = partida.getJugadores().get(0);
        for (Carta carta : humano.getMano()) {
            Button boton = new Button(carta.toString());
            boton.getStyleClass().add("carta-boton");
            // Se habilita y se conecta con la lógica de jugar en la segunda
            // mitad del controlador, para no mezclar renderizado con
            // interacción en este paso.
            boton.setDisable(true);
            hboxManoHumano.getChildren().add(boton);
        }
    }

    private void actualizarEtiquetasMaquinas() {
        List<Jugador> jugadores = partida.getJugadores();
        int indiceMaquina = 0;
        for (Jugador jugador : jugadores) {
            if (!jugador.esMaquina()) {
                continue;
            }
            Label etiqueta = etiquetasMaquinas.get(indiceMaquina++);
            String estado = jugador.isEliminado()
                    ? "eliminado"
                    : jugador.cantidadCartas() + " cartas";
            etiqueta.setText(jugador.getNombre() + ": " + estado);
        }
    }

    @FXML
    private void manejarSalir() {
        Platform.exit();
    }
}
