package com.example.miniproyecto3poecincuentazo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
public class JuegoController {

    private static final int ESPERA_MAQUINA_MIN_MS = 2000;

    private static final int ESPERA_MAQUINA_MAX_MS = 4000;

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

    private final AtomicInteger segundosTurno = new AtomicInteger(0);
    private volatile boolean juegoActivo = false;
    private Thread hiloCronometro;

    /**
     *
     * @param partidaIniciada la partida ya preparada (con {@link Partida#iniciar()} ya invocado)
     * @throws NullPointerException si {@code partidaIniciada} es nula
     */
    public void iniciarPartida(Partida partidaIniciada) {
        this.partida = Objects.requireNonNull(partidaIniciada, "La partida no puede ser nula");
        construirEtiquetasMaquinas();
        juegoActivo = true;
        iniciarHiloCronometro();
        procesarTurno();
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

    // ------------------------------------------------------------------
    // Flujo de turnos (parte 4)
    // ------------------------------------------------------------------

    private void procesarTurno() {
        while (!partida.isTerminada() && partida.verificarYEliminarSiNoPuedeJugar()) {
            // El jugador actual no tenía jugada válida: ya quedó eliminado y
            // Partida avanzó el turno internamente. Seguimos revisando al
            // siguiente por si tampoco puede jugar.
        }

        actualizarVista();

        if (partida.isTerminada()) {
            finalizarJuego();
            return;
        }

        segundosTurno.set(0);
        labelTiempo.setText("Tiempo de turno: 0s");

        if (partida.getJugadorActual().esMaquina()) {
            iniciarTurnoMaquina();
        }
        // Si le toca al humano no hay que hacer nada más: actualizarVista() ya
        // dejó habilitados, en actualizarManoHumano(), los botones de las
        // cartas jugables.
    }

    private void iniciarTurnoMaquina() {
        Thread hiloTurnoMaquina = new Thread(() -> {
            int esperaMs = ThreadLocalRandom.current().nextInt(ESPERA_MAQUINA_MIN_MS, ESPERA_MAQUINA_MAX_MS + 1);
            try {
                Thread.sleep(esperaMs);
            } catch (InterruptedException interrumpido) {
                Thread.currentThread().interrupt();
                return;
            }
            Platform.runLater(this::ejecutarJugadaMaquina);
        }, "hilo-turno-maquina");
        hiloTurnoMaquina.setDaemon(true);
        hiloTurnoMaquina.start();
    }

    private void ejecutarJugadaMaquina() {
        if (!juegoActivo || partida.isTerminada()) {
            return;
        }
        Jugador actual = partida.getJugadorActual();
        if (!actual.esMaquina()) {
            // Resguardo defensivo: no debería pasar, el turno no cambia mientras
            // el hilo espera porque solo el hilo de JavaFX toca la partida.
            procesarTurno();
            return;
        }
        JugadorMaquina maquina = (JugadorMaquina) actual;
        Carta elegida = maquina.elegirCarta(partida.getSumaMesa());
        if (elegida == null) {
            // También defensivo: procesarTurno() ya filtró a los jugadores sin
            // jugada válida antes de llegar aquí.
            partida.verificarYEliminarSiNoPuedeJugar();
        } else {
            partida.jugarCartaJugadorActual(elegida, null);
        }
        procesarTurno();
    }

    /**
     *
     * @param carta la carta que el jugador humano hizo clic para jugar
     */
    private void manejarClickCartaHumano(Carta carta) {
        if (partida.isTerminada() || partida.getJugadorActual().esMaquina()) {
            return;
        }

        Integer valorElegido = resolverValorHumano(carta);
        if (valorElegido == null && carta.getValor().tieneValorAmbiguo() && !carta.esJugable(partida.getSumaMesa())) {
            // El As no tiene ningún valor válido; no debería ocurrir porque el
            // botón estaría deshabilitado, pero se deja como resguardo.
            mostrarAlerta(Alert.AlertType.WARNING, "Jugada inválida",
                    "Esa carta excede la suma máxima de 50 con la suma actual de la mesa.");
            return;
        }

        try {
            partida.jugarCartaJugadorActual(carta, valorElegido);
        } catch (CartaInvalidaException error) {
            mostrarAlerta(Alert.AlertType.WARNING, "Jugada inválida", error.getMessage());
            return;
        }
        procesarTurno();
    }

    /**
     *
     * @param carta la carta que el humano va a jugar
     * @return el valor elegido, o {@code null} si no aplica o no hay ninguna opción válida
     */
    private Integer resolverValorHumano(Carta carta) {
        if (!carta.getValor().tieneValorAmbiguo()) {
            return null;
        }
        int sumaActual = partida.getSumaMesa();
        List<Integer> opcionesValidas = new ArrayList<>();
        for (int posible : carta.getValoresPosibles()) {
            if (sumaActual + posible <= Partida.SUMA_MAXIMA) {
                opcionesValidas.add(posible);
            }
        }
        if (opcionesValidas.isEmpty()) {
            return null;
        }
        if (opcionesValidas.size() == 1) {
            return opcionesValidas.get(0);
        }
        return preguntarValorAs();
    }

    /**
     *
     * @return 1 o 10, según el botón que el jugador haya elegido (1 por
     *         defecto si cierra el diálogo sin elegir)
     */
    private Integer preguntarValorAs() {
        Alert dialogo = new Alert(Alert.AlertType.CONFIRMATION);
        dialogo.setTitle("As");
        dialogo.setHeaderText("¿Cómo quieres jugar el As?");
        dialogo.setContentText("Elige el valor que se sumará a la mesa.");

        ButtonType botonUno = new ButtonType("Sumar 1");
        ButtonType botonDiez = new ButtonType("Sumar 10");
        dialogo.getButtonTypes().setAll(botonUno, botonDiez);

        Optional<ButtonType> resultado = dialogo.showAndWait();
        return (resultado.isPresent() && resultado.get() == botonDiez) ? 10 : 1;
    }

    private void finalizarJuego() {
        juegoActivo = false;
        detenerHiloCronometro();
        deshabilitarManoHumano();

        Jugador ganador = partida.getGanador();
        String mensaje = ganador != null
                ? "¡" + ganador.getNombre() + " ha ganado la partida!"
                : "La partida terminó sin un jugador restante.";
        labelTurno.setText(mensaje);
        mostrarAlerta(Alert.AlertType.INFORMATION, "Fin del juego", mensaje);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void deshabilitarManoHumano() {
        for (var nodo : hboxManoHumano.getChildren()) {
            nodo.setDisable(true);
        }
    }

    // ------------------------------------------------------------------
    // Cronómetro (segundo hilo)
    // ------------------------------------------------------------------

    private void iniciarHiloCronometro() {
        hiloCronometro = new Thread(() -> {
            while (juegoActivo) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interrumpido) {
                    Thread.currentThread().interrupt();
                    return;
                }
                if (!juegoActivo) {
                    return;
                }
                int segundos = segundosTurno.incrementAndGet();
                Platform.runLater(() -> labelTiempo.setText("Tiempo de turno: " + segundos + "s"));
            }
        }, "hilo-cronometro");
        hiloCronometro.setDaemon(true);
        hiloCronometro.start();
    }

    private void detenerHiloCronometro() {
        if (hiloCronometro != null) {
            hiloCronometro.interrupt();
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
        boolean esTurnoHumano = !partida.isTerminada() && partida.getJugadorActual() == humano;

        for (Carta carta : humano.getMano()) {
            Button boton = new Button(carta.toString());
            boton.getStyleClass().add("carta-boton");
            boolean jugable = esTurnoHumano && carta.esJugable(partida.getSumaMesa());
            boton.setDisable(!jugable);
            boton.setOnAction(evento -> manejarClickCartaHumano(carta));
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
        juegoActivo = false;
        detenerHiloCronometro();
        Platform.exit();
    }
}
