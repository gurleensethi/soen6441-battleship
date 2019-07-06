package com.soen6441.battleship.viewmodels.playerviewmodel;

import com.soen6441.battleship.common.Constants;
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

        HBox root = new HBox(50);
        VBox start = new VBox();
        GridPane enemyBoard = new GridPane();
        GridPane playerBoard = new GridPane();
        int gridSize ;

        public void createGame(Stage primaryStage) {

            Button startButton = new Button();

            Scene scene2 = new Scene(root, 700, 400);// starting page of the application

            startButton.setAlignment(Pos.CENTER);
            TextField gridSizeTxtField = new TextField();
            gridSizeTxtField.setAlignment(Pos.BOTTOM_CENTER);
            startButton.setText("Start Game");
            start.getChildren().addAll(new Label("Enter the grid size:"), gridSizeTxtField, startButton);
            gridSizeTxtField.setMaxSize(100,10);
            start.setAlignment(Pos.CENTER);
            gridSizeTxtField.textProperty().addListener((obs, oldText, newText) -> {
                if (newText.matches("^(0|[1-9][0-9]*)$")) {
                    gridSizeTxtField.setText(newText);
                } else {
                    gridSizeTxtField.setText("");
                }
            });
            startButton.setOnMouseClicked(e ->{
                gridSize = Integer.parseInt(gridSizeTxtField.getText());
                createGrids(gridSize);
                primaryStage.setScene(scene2);
            });

            //Create scenes of the primary stage
            Scene scene1 = new Scene(start, 700, 400);
            primaryStage.setScene(scene1);
            primaryStage.show();
        }

        private void createGrids(int gridSize){

            for (int rows = 0; rows < gridSize; rows++) {
                for (int columns = 0; columns < gridSize; columns++) {
                    Button button = new Button();
                    button.setText(" ");
                    button.setOnMouseClicked(e -> button.setStyle("-fx-background-color: #ff0040"));
                    enemyBoard.add(button, columns, rows);
                }
            }

            for (int rows = 0; rows < gridSize; rows++) {
                for (int columns = 0; columns < gridSize; columns++) {
                    Button button1 = new Button();
                    button1.setText(" ");
                    button1.setOnMouseClicked(e -> button1.setStyle("-fx-background-color: #ff0040"));
                    playerBoard.add(button1, columns, rows);
                }
            }

            root.getChildren().addAll(playerBoard, enemyBoard);
        }
}
