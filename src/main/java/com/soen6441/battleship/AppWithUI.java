package com.soen6441.battleship;

import com.soen6441.battleship.common.Constants;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;


public class AppWithUI {

        HBox root = new HBox(50);
        VBox start = new VBox();
        GridPane enemyBoard = new GridPane();
        GridPane playerBoard = new GridPane();
        int gridSize ;

        private static Button carrierButton;
        private static Button battleshipButton;
        private static Button cruiserButton;
        private static Button submarineButton;
        private static Button destroyerButton;
        private static Button clearLastButton;
        private static Button clearAllButton;
        private static Button finalizeButton;
        private static Button rotateButton;
        private static Button rollButton;

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
                createButtons();
                primaryStage.setScene(scene2);
            });


            //Create scenes of the primary stage
            Scene scene1 = new Scene(start, 700, 400);
            primaryStage.setScene(scene1);
            primaryStage.show();
        }


        private GridPane getGrid(int gridSize){
            GridPane innerGridPane = new GridPane();
            for (int rows = 0; rows < gridSize; rows++) {
                for (int columns = 0; columns < gridSize; columns++) {
                    Button button = new Button();
                    button.setOnMouseClicked(e -> button.setStyle("-fx-background-color: #ff0040"));
                    innerGridPane.add(button, columns, rows);
                }
            }
            return innerGridPane;
        }

        private void createGrids(int gridSize){
            enemyBoard = getGrid(gridSize);
            playerBoard = getGrid(gridSize);
            root.getChildren().addAll(playerBoard, enemyBoard);
        }

        private void createButtons(){

            carrierButton = new Button("Carrier");
            battleshipButton = new Button("Battleship");
            cruiserButton = new Button("Cruiser");
            submarineButton = new Button("Submarine");
            destroyerButton = new Button("Destroyer");

            HBox shipBox = new HBox();

            shipBox.getChildren().addAll(carrierButton,battleshipButton,cruiserButton,submarineButton,destroyerButton);
            shipBox.setAlignment(Pos.CENTER);
            shipBox.setPadding(new javafx.geometry.Insets(20, 0, 0, 0));


            clearLastButton = new Button("Clear Last");
            clearAllButton = new Button("Clear All");
            finalizeButton = new Button("Finalize");
            rotateButton = new Button("Rotate");
            rollButton = new Button("Roll");

            HBox rotateBox = new HBox();
            rotateBox.getChildren().addAll(rotateButton, clearLastButton, clearAllButton, finalizeButton, rollButton);
            rotateBox.setAlignment(Pos.CENTER);

            VBox combined = new VBox();
            combined.getChildren().addAll(shipBox, rotateBox);
            combined.setAlignment(Pos.BOTTOM_CENTER);
            combined.setPadding(new javafx.geometry.Insets(0,200,0,0));

            root.getChildren().addAll(combined);
        }
}
