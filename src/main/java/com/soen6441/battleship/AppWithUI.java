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

            //Start screen button
            Button startButton = new Button();
            startButton.setText("Start Game");
            startButton.setAlignment(Pos.CENTER);

            TextField gridSizeTxtField = new TextField();
            gridSizeTxtField.setAlignment(Pos.TOP_CENTER);
            gridSizeTxtField.setMaxSize(100,10);

            gridSizeTxtField.textProperty().addListener((obs, oldText, newText) -> {
                if (newText.matches("^(0|[1-9][0-9]*)$")) {
                    gridSizeTxtField.setText(newText);
                } else {
                    gridSizeTxtField.setText("");
                }
            });

            //Adding Label,text-field and button to Start Scene
            start.getChildren().addAll(new Label("Enter the grid size:"), gridSizeTxtField, startButton);
            start.setAlignment(Pos.CENTER);

            //Start button action
            startButton.setOnMouseClicked(e ->{
                gridSize = Integer.parseInt(gridSizeTxtField.getText());
                createGrids(gridSize);
                createButtons();
                Scene scene2 = new Scene(root, 1000, 800);// starting page of the application
                primaryStage.setScene(scene2);
            });


            //Create scenes of the primary stage
            Scene scene1 = new Scene(start, 1000, 800);
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

            //Initialize ship buttons
            carrierButton = new Button("Carrier");
            battleshipButton = new Button("Battleship");
            cruiserButton = new Button("Cruiser");
            submarineButton = new Button("Submarine");
            destroyerButton = new Button("Destroyer");

            //Initialize option menu buttons
            clearLastButton = new Button("Clear Last");
            clearAllButton = new Button("Clear All");
            finalizeButton = new Button("Finalize");
            rotateButton = new Button("Rotate");
            rollButton = new Button("Randomize");

            setButtonDimensions();

            HBox shipBox = new HBox(); // Horizontal Box to hold ship buttons

            shipBox.getChildren().addAll(carrierButton,battleshipButton,cruiserButton,submarineButton,destroyerButton);
            shipBox.setAlignment(Pos.BOTTOM_CENTER);


            VBox allOptionBox = new VBox(); // Vertical Box to hold options

            allOptionBox.getChildren().addAll(rotateButton, clearLastButton, clearAllButton, finalizeButton, rollButton);
            allOptionBox.setAlignment(Pos.TOP_RIGHT);

            root.getChildren().addAll(shipBox, allOptionBox);
        }

        private static void setButtonDimensions(){

            //setting min heights
            carrierButton.setMinWidth(80);
            battleshipButton.setMinWidth(80);
            cruiserButton.setMinWidth(80);
            submarineButton.setMinWidth(80);
            destroyerButton.setMinWidth(80);

            //setting min heights
            clearLastButton.setMinWidth(80);
            clearAllButton.setMinWidth(80);
            finalizeButton.setMinWidth(80);
            rotateButton.setMinWidth(80);
            rollButton.setMinWidth(80);

        }
}
