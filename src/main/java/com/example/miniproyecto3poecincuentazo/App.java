package com.example.miniproyecto3poecincuentazo;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Juan Diego Quiñones
 * @author Jeferson Gomez Gomez
 * @version 1.0
 */
public class App extends Application {

    private static final String VISTA_CONFIGURACION = "/com/example/miniproyecto3poecincuentazo/configuracion.fxml";

    @Override
    public void start(Stage stagePrincipal) {
        SceneNavigator.inicializar(stagePrincipal);
        SceneNavigator.cambiarEscena(VISTA_CONFIGURACION, "Cincuentazo");
        stagePrincipal.show();
    }

    /**
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
