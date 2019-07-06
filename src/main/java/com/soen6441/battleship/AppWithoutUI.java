package com.soen6441.battleship;

import com.soen6441.battleship.services.gamegrid.GameGrid;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;


public class AppWithoutUI {
    public static void main(String[] args) {
        GameGrid gameGrid = new GameGrid(8);
        Ship ship1 = new Ship.Builder()
                .setStartCoordinates(0, 0)
                .setEndCoordinates(4, 0)
                .setDirection(ShipDirection.HORIZONTAL)
                .build();

        Ship ship2 = new Ship.Builder()
                .setStartCoordinates(4, 0)
                .setEndCoordinates(4, 4)
                .setDirection(ShipDirection.VERTICAL)
                .build();

        try {
            gameGrid.placeShip(ship1);
            gameGrid.placeShip(ship2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
