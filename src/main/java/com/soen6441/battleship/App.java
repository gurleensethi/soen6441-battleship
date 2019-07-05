package com.soen6441.battleship;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Hello world!
 */
public class App extends Application {

    Stage window;
    Scene scene1, scene2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Battleship Classic");

        //Welcome Screen of the game
        Label label1 =  new Label("Welcome to the BattleShip Game");
        Button button = new Button("Start Game");
        button.setOnAction(e -> window.setScene(scene2));
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label1, button);
        scene1 = new Scene(layout1,300,250);

        Label label2 = new Label("Click on the place ships button to place ships!");
        Button button2 = new Button("Place Ships");
        Button exit = new Button("Exit");
        exit.setAlignment(Pos.BOTTOM_RIGHT);
        exit.setOnAction(e-> Platform.exit());
        button2.setOnAction(e-> System.out.println("I do nothing for now"));
        HBox layout2 = new HBox(30);
        layout2.getChildren().addAll(label2, button2, exit);
        scene2 = new Scene(layout2, 600, 400);

        window.setScene(scene1);
        window.setResizable(false);
        window.show();
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