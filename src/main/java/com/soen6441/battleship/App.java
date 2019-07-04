package com.soen6441.battleship;

import com.soen6441.battleship.data.model.Grid;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        new GameController().start();

        final GameGrid gameGrid = new GameGrid.Builder()
                .setDimensions(8, 8)
                .build();

//        DaggerCoffeeShop.builder().build().maker().makeCoffee();
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