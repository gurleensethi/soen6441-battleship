package com.soen6441.battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

public class App extends Application {
    final private Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        launch(args);
    }
    @Override

    public void start(Stage primaryStage) throws Exception {
        File file = new File("src/main/resources/WelcomeScreen.fxml");
        Parent root = FXMLLoader.load(file.toURI().toURL());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }
}