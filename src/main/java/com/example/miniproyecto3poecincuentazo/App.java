package com.example.miniproyecto3poecincuentazo;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private static final String VISTA_CONFIGURACION = "/com/cincuentazo/view/configuracion.fxml";

    @Override
    public void start(Stage stagePrincipal) {
        SceneNavigator.inicializar(stagePrincipal);
        SceneNavigator.cambiarEscena(VISTA_CONFIGURACION, "Cincuentazo");
        stagePrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
