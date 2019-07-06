package com.soen6441.battleship;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppWithUI {

    public void createGame(Stage primaryStage) {

        HBox root = new HBox(50);
        VBox start = new VBox();

        GridPane enemyBoard = new GridPane();
        GridPane playerBoard = new GridPane();

        for (int rows = 0; rows < 10; rows++) {
            for (int columns = 0; columns < 10; columns++) {
                Button button = new Button();
                button.setText(" ");
                button.setOnMouseClicked(e -> button.setStyle("-fx-background-color: #ff0040"));
                enemyBoard.add(button, columns, rows);
            }
        }

        for (int rows = 0; rows < 10; rows++) {
            for (int columns = 0; columns < 10; columns++) {
                Button button1 = new Button();
                button1.setText(" ");
                button1.setOnMouseClicked(e -> button1.setStyle("-fx-background-color: #ff0040"));
                playerBoard.add(button1, columns, rows);
            }
        }
        root.getChildren().addAll(playerBoard, enemyBoard);
        //Create scenes of the primary stage
        Scene scene1 = new Scene(start, 700, 400);// starting page of the application
        Button startButton = new Button();
        Scene scene2 = new Scene(root, 700, 400);// Staging area to place ships


        startButton.setOnAction(e -> primaryStage.setScene(scene2));
        startButton.setText("Start Game");
        startButton.setAlignment(Pos.CENTER);
        start.getChildren().add(startButton);
        start.getChildren().add(new Label("Enter the grid size:"));
        TextField gridSize = new TextField();
        gridSize.setAlignment(Pos.BOTTOM_CENTER);
        start.getChildren().add(gridSize);


        primaryStage.setScene(scene1);
        primaryStage.show();
    }
}