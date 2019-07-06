package com.soen6441.battleship;


import com.soen6441.battleship.viewmodels.playerviewmodel.AppWithUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.input.*;
import javafx.scene.layout.*;



/**
 * Hello world!
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("Battleship");
        AppWithUI uiObject = new AppWithUI();
        uiObject.createGame(primaryStage);


    }
}