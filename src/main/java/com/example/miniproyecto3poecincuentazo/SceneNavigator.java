package com.example.miniproyecto3poecincuentazo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class SceneNavigator {

    private static Stage stagePrincipal;

    private SceneNavigator() {
        // Clase utilitaria: no se instancia.
    }

    public static void inicializar(Stage stage) {
        stagePrincipal = stage;
    }

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
