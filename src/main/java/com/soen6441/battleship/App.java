package com.soen6441.battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

/**
 * The type App is the class which is main start to the application and has the main method,
 * it sets the primary stage for the game and passes to AppwithGui class.
 */
public class App extends Application {
    final private Logger logger = Logger.getLogger(App.class.getName());

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}