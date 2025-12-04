package com.example.sea;

import com.example.sea.gui.ScreenManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        ScreenManager.getInstance().setPrimaryStage(stage);

        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setResizable(false);

        ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
    }

    public static void main(String[] args) {
        launch();
    }
}