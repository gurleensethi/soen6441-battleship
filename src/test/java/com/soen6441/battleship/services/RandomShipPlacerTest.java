package com.soen6441.battleship.services;

import com.soen6441.battleship.services.boardgenerator.RandomShipPlacer;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RandomShipPlacerTest {
    private GameGrid gameGrid;

    @Before
    public void setUp() {
        gameGrid = new GameGrid(8);
    }

    @Test
    public void shipsArePlaceOnBoard() {
        RandomShipPlacer randomShipPlacer = new RandomShipPlacer();
        randomShipPlacer.placeRandomShips(gameGrid);
        assertEquals(5, gameGrid.getShips().size());
    }
}
