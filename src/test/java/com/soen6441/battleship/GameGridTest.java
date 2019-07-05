package com.soen6441.battleship;

import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.exceptions.DirectionCoordinatesMismatchException;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class GameGridTest {
    private GameGrid gameGrid;
    private Ship wrongShipHorizontal;
    private Ship wrongShipVertical;
    private Ship wrongShipStart;
    private Ship wrongShipEnd;
    private Ship correctShip;

    @Before()
    public void setUp() {
        gameGrid = new GameGrid(8);

        wrongShipHorizontal = new Ship.Builder()
                .setDirection(ShipDirection.HORIZONTAL)
                .setStartCoordinates(1, 1)
                .setEndCoordinates(2, 2)
                .build();

        wrongShipVertical = new Ship.Builder()
                .setDirection(ShipDirection.VERTICAL)
                .setStartCoordinates(1, 1)
                .setEndCoordinates(2, 2)
                .build();

        wrongShipStart = new Ship.Builder()
                .setDirection(ShipDirection.HORIZONTAL)
                .setStartCoordinates(-1, -1)
                .setEndCoordinates(1, -1)
                .build();

        wrongShipEnd = new Ship.Builder()
                .setDirection(ShipDirection.HORIZONTAL)
                .setStartCoordinates(1, 1)
                .setEndCoordinates(11, 1)
                .setName("Wrong Ship from Y")
                .setUniqueId(UUID.randomUUID().toString())
                .build();

        correctShip = new Ship.Builder()
                .setDirection(ShipDirection.HORIZONTAL)
                .setStartCoordinates(1, 1)
                .setEndCoordinates(6, 1)
                .setName("Correct Ship")
                .setUniqueId(UUID.randomUUID().toString())
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void throwsExceptionIfShipIsNull() throws Exception {
        gameGrid.placeShip(null);
    }

    @Test(expected = CoordinatesOutOfBoundsException.class)
    public void throwsExceptionOnWrongCoordinatesX() throws Exception {
        gameGrid.placeShip(wrongShipStart);
    }

    @Test(expected = CoordinatesOutOfBoundsException.class)
    public void throwsExceptionOnWrongCoordinatesY() throws Exception {
        gameGrid.placeShip(wrongShipEnd);
    }

    @Test(expected = DirectionCoordinatesMismatchException.class)
    public void throwsExceptionOnWrongDirectionHorizontal() throws Exception {
        gameGrid.placeShip(wrongShipHorizontal);
    }

    @Test(expected = DirectionCoordinatesMismatchException.class)
    public void throwsExceptionOnWrongDirectionVertical() throws Exception {
        gameGrid.placeShip(wrongShipVertical);
    }

    @Test()
    public void placesShipCorrectly() throws Exception {
        gameGrid.placeShip(correctShip);
    }
}
