package com.soen6441.battleship;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.exceptions.DirectionCoordinatesMismatchException;
import com.soen6441.battleship.exceptions.InvalidShipPlacementException;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameGridTest {
    private GameGrid gameGrid;
    private static Ship wrongShipHorizontal;
    private static Ship wrongShipVertical;
    private static Ship wrongShipStart;
    private static Ship wrongShipEnd;
    private static Ship correctShip;
    private static Ship correctShip2;
    private static Ship overlappingShip;

    @Before()
    public void setUp() {
        gameGrid = new GameGrid(8);
    }

    @BeforeClass()
    public static void setUpBeforeClass() {
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
                .build();

        correctShip = new Ship.Builder()
                .setDirection(ShipDirection.HORIZONTAL)
                .setStartCoordinates(1, 1)
                .setEndCoordinates(6, 1)
                .build();

        correctShip2 = new Ship.Builder()
                .setDirection(ShipDirection.VERTICAL)
                .setStartCoordinates(0, 2)
                .setEndCoordinates(0, 5)
                .build();

        overlappingShip = new Ship.Builder()
                .setDirection(ShipDirection.VERTICAL)
                .setStartCoordinates(3, 0)
                .setEndCoordinates(3, 5)
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

    @Test(expected = InvalidShipPlacementException.class)
    public void throwsExceptionIfOverlapping() throws Exception {
        gameGrid.placeShip(correctShip);
        gameGrid.placeShip(overlappingShip);
    }

    @Test()
    public void placesShipCorrectly() throws Exception {
        gameGrid.placeShip(correctShip);
    }

    @Test()
    public void places2ndShipCorrectly() throws Exception {
        gameGrid.placeShip(correctShip2);
    }

    @Test()
    public void hitsOnAShip() throws Exception {
        gameGrid.placeShip(correctShip);
        HitResult result = gameGrid.hit(3, 1);
        assertEquals(HitResult.HIT, result);
    }

    @Test()
    public void hitMissOnAWrongCoordinate() throws Exception {
        gameGrid.placeShip(correctShip);
        HitResult result = gameGrid.hit(3, 2);
        assertEquals(HitResult.MISS, result);
    }

    @Test(expected = CoordinatesOutOfBoundsException.class)
    public void throwsExceptionOnWrongCoordinates() throws Exception {
        gameGrid.placeShip(correctShip);
        gameGrid.hit(-1, 0);
    }

    @Test()
    public void detectsIfHitWasAlreadyMadeOnCoordinate() throws Exception {
        gameGrid.placeShip(correctShip);
        HitResult result1 = gameGrid.hit(0, 0);
        assertEquals(HitResult.MISS, result1);
        HitResult result2 = gameGrid.hit(0, 0);
        assertEquals(HitResult.ALREADY_HIT, result2);
    }

    @Test()
    public void gridObservableUpdatesGridOnAddingShip() throws Exception {
        Observable<Grid> gridObservable = gameGrid.getGridAsObservable();
        TestObserver<Grid> testObserver = new TestObserver<>();
        gameGrid.placeShip(correctShip);
        gridObservable.subscribe(testObserver);
        testObserver.assertValue(updatedGrid -> updatedGrid.getCellState(1, 1) == CellState.SHIP);
    }


}
