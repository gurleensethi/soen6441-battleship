package com.soen6441.battleship;


import javafx.application.Application;
import javafx.stage.Stage;


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