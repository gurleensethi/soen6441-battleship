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
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Hello world!
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Battleship Classic");
        Button btn = new Button("Console print");
        btn.setLayoutX(250);
        btn.setLayoutY(220);
        btn.setOnAction(event -> System.out.println("You pressed the button."));

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.setResizable(false);
        primaryStage.show();
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