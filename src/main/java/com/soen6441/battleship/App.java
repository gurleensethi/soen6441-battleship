package com.soen6441.battleship;


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
        HBox root = new HBox(50);
        StackPane start = new StackPane();
        GridPane enemyBoard = new GridPane();
        GridPane playerBoard = new GridPane();

        for (int rows = 0; rows < 10; rows++) {
            for (int columns = 0; columns < 10; columns++) {
                Button enemyCell = new Button();
                enemyCell.setText(" ");
                enemyCell.setOnMouseClicked(e -> enemyCell.setStyle("-fx-background-color: #ff0040"));
                enemyBoard.add(enemyCell, columns, rows);
            }
        }

        for (int rows = 0; rows < 10; rows++) {
            for (int columns = 0; columns < 10; columns++) {
                Button playerCell = new Button();
                playerCell.setText(" ");
                playerCell.setOnMouseClicked(e -> playerCell.setStyle("-fx-background-color: #ff0040"));
                playerBoard.add(playerCell, columns, rows);

            }
        }
        root.getChildren().addAll(playerBoard, enemyBoard);

        // Display start scene
        Scene scene1 = new Scene(start, 700, 400);

        // Display Game-Play scene
        Scene scene2 = new Scene(root, 700, 400);


        Button startButton = new Button();
        startButton.setOnAction(e -> primaryStage.setScene(scene2));
        startButton.setText("Start Game");
        startButton.setAlignment(Pos.CENTER);
        start.getChildren().add(startButton);

        primaryStage.setScene(scene1);
        primaryStage.show();


    }
}