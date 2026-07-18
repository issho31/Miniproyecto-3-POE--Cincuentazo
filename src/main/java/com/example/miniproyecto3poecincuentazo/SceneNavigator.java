package com.example.miniproyecto3poecincuentazo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
public final class SceneNavigator {

    private static Stage stagePrincipal;

    private SceneNavigator() {
        // Clase utilitaria: no se instancia.
    }

    /**
     *
     * @param stage el Stage principal de la aplicación
     */
    public static void inicializar(Stage stage) {
        stagePrincipal = stage;
    }

    /**
     *
     * @param rutaFxml ruta del recurso FXML relativa al classpath, p. ej.
     *                 {@code "/com/example/miniproyecto3poecincuentazo/juego.fxml"}
     * @param titulo   título que tomará la ventana
     * @param <T>       tipo del controlador declarado en el FXML
     * @return el controlador asociado al FXML recién cargado, para poder
     *         inyectarle datos (por ejemplo, la {@code Partida} ya iniciada)
     * @throws IllegalStateException si el FXML no se pudo cargar
     */
    public static <T> T cambiarEscena(String rutaFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(rutaFxml));
            Parent raiz = loader.load();
            Scene escena = new Scene(raiz);
            stagePrincipal.setScene(escena);
            stagePrincipal.setTitle(titulo);
            stagePrincipal.centerOnScreen();
            return loader.getController();
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar la vista: " + rutaFxml, e);
        }
    }
}
