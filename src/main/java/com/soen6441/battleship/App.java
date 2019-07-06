package com.soen6441.battleship;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Group;



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

        for(int rows =0; rows<10;rows++){
            for(int columns=0;columns<10;columns++){
                Button button = new Button();
                button.setText(" ");
                button.setOnMouseClicked(e -> button.setStyle("-fx-background-color: #ff0040"));
                enemyBoard.add(button,columns,rows);

            }
        }

        for(int rows =0; rows<10;rows++){
            for(int columns=0;columns<10;columns++){
                Button button1 = new Button();
                button1.setText(" ");
                button1.setOnMouseClicked(e -> button1.setStyle("-fx-background-color: #ff0040"));
                playerBoard.add(button1,columns,rows);

            }
        }
        root.getChildren().addAll(playerBoard,enemyBoard);


        Scene scene1 = new Scene(start, 700, 400);
        Scene scene2 = new Scene(root, 700, 400);
        Button startButton = new Button();
        startButton.setOnAction(e-> primaryStage.setScene(scene2));
        startButton.setText("Start Game");
        startButton.setAlignment(Pos.CENTER);
        start.getChildren().add(startButton);
        primaryStage.setScene(scene1);
        primaryStage.show();









//
//        MenuBar menuBar = new MenuBar();
//        menuBar.prefWidthProperty().bind(stage.widthProperty());
//
//        Menu gameMenu = new Menu("_Game");
//        newGame = new Menu("_New Game");
//
//
//            stage = primaryStage;
//            stage.setTitle("Battleship -");
//            stage.setResizable(false);
//
//
//            carrierButton = new Button("Carrier");
//            battleshipButton = new Button("Battleship");
//            cruiserButton = new Button("Cruiser");
//            submarineButton = new Button("Submarine");
//            destroyerButton = new Button("Destroyer");
//
//
//            HBox shipBox = new HBox();
//
//            shipBox.getChildren().addAll(carrierButton,battleshipButton,cruiserButton,submarineButton,destroyerButton);
//            shipBox.setAlignment(Pos.CENTER);
//            shipBox.setPadding(new Insets(20, 0, 0, 0));
//
//
//            clearLastButton = new Button("Clear Last");
//            clearAllButton = new Button("Clear All");
//            finalizeButton = new Button("Finalize");
//            rotateButton = new Button("Rotate");
//            rollButton = new Button("Roll");
//
//
//            p1Ready.setTextFill(Color.RED);
//            p2Ready.setTextFill(Color.RED);
//
//            HBox rotateBox = new HBox();
//            rotateBox.getChildren().addAll(rotateButton, clearLastButton, clearAllButton, finalizeButton, rollButton);
//            rotateBox.setAlignment(Pos.CENTER);
//
//
//            HBox p1TurnBox = new HBox();
//            p1TurnBox.getChildren().add(p1Turn);
//            p1TurnBox.setPadding(new Insets(0, 0, 0, 10));
//
//            HBox p1ReadyBox = new HBox();
//            p1ReadyBox.getChildren().add(p1Ready);
//
//            HBox roll1Box = new HBox();
//            roll1Box.getChildren().add(roll1);
//            roll1Box.setPadding(new Insets(0, 10, 0, 0));
//
//            Region region1 = new Region();
//            HBox.setHgrow(region1, Priority.ALWAYS);
//
//            Region region2 = new Region();
//            HBox.setHgrow(region2, Priority.ALWAYS);
//
//            p1StatBox.getChildren().addAll(p1TurnBox, region1, p1ReadyBox, region2, roll1Box);
//
//            VBox bottomGridBox = new VBox();
//            //bottomGridBox.getChildren().addAll(bottomGrid.getGridPane(), p1StatBox);
//
//
//            Region region3 = new Region();
//            HBox.setHgrow(region3, Priority.ALWAYS);
//
//            Region region4 = new Region();
//            HBox.setHgrow(region4, Priority.ALWAYS);
//
//            HBox p2TurnBox = new HBox();
//            p2TurnBox.getChildren().add(p2Turn);
//            p2TurnBox.setPadding(new Insets(0, 0, 0, 10));
//
//            HBox p2ReadyBox = new HBox();
//            p2ReadyBox.getChildren().add(p2Ready);
//            p2ReadyBox.setPadding(new Insets(0, 0, 0, 10));
//
//            HBox roll2Box = new HBox();
//            roll2Box.getChildren().add(roll2);
//            roll2Box.setPadding(new Insets(0, 10, 0, 0));
//
//            p2StatBox.getChildren().addAll(p2TurnBox, region3, p2ReadyBox, region4, roll2Box);
//
//            VBox topGridBox = new VBox();
//           // topGridBox.getChildren().addAll(topGrid.getGridPane(), p2StatBox);
//
//            gridBox.getChildren().addAll(bottomGridBox, topGridBox);
//
//            gridBox.setVisible(false);
//
//            VBox combined = new VBox();
//            combined.getChildren().addAll(menuBar, gridBox, shipBox, rotateBox);
//            combined.setAlignment(Pos.CENTER);
//            combined.setPadding(new Insets(0, 0, 10, 0));
//
//
//            root.getChildren().addAll(combined);
//
//
//            Scene newScene = new Scene(root, Color.LIGHTGRAY);
//
//
//            stage.setScene(newScene);
//            stage.show();
//
//            initialStageWidth = stage.getWidth();
//            initialStageHeight = stage.getHeight();
//
//            System.out.println(newScene.getHeight());

    }
    }

//@Singleton()
//class ElectricHeater implements Heater {
//    @Inject()
//    public ElectricHeater() {
//        System.out.println("Creating new heater...");
//    }
//
//    @Override
//    public boolean isHot() {
//        return false;
//    }
//
//    @Override
//    public void on() {
//
//    }
//
//    @Override
//    public void off() {
//
//    }
//}
//
//class Thermosiphon {
//    private Heater heater;
//
//    @Inject()
//    public Thermosiphon(Heater heater) {
//        this.heater = heater;
//    }
//}
//
//class CoffeeMaker {
//    private ElectricHeater heater;
//    private Thermosiphon thermosiphon;
//
//    @Inject()
//    public CoffeeMaker(ElectricHeater heater, Thermosiphon thermosiphon) {
//        this.heater = heater;
//        this.thermosiphon = thermosiphon;
//    }
//
//    void makeCoffee() {
//        System.out.println("Making coffee from Dagger!");
//    }
//}
//
//@Singleton
//@Component(modules = {CoffeeMachineModule.class})
//interface CoffeeShop {
//    CoffeeMaker maker();
//}
//
//interface Heater {
//    boolean isHot();
//
//    void on();
//
//    void off();
//}
//
//@Module()
//class CoffeeMachineModule {
//    @Singleton()
//    @Provides()
//    Heater heaterProvider() {
//        return new ElectricHeater();
//    }
//}