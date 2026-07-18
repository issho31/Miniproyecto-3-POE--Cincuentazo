module com.example.miniproyecto3poecincuentazo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.miniproyecto3poecincuentazo to javafx.fxml;
    exports com.example.miniproyecto3poecincuentazo;
}