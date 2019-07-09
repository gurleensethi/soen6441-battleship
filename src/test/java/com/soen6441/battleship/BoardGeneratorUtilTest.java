package com.soen6441.battleship;

import com.soen6441.battleship.services.gamegrid.GameGrid;
import com.soen6441.battleship.utils.BoardGeneratorUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardGeneratorUtilTest {
    private GameGrid gameGrid;

    @Before
    public void setUp() {
        gameGrid = new GameGrid(8);
    }

    @Test
    public void shipsArePlaceOnBoard() {
        BoardGeneratorUtil boardGeneratorUtil = new BoardGeneratorUtil();
        boardGeneratorUtil.placeRandomShips(gameGrid);
        assertEquals(5, gameGrid.getShips().size());
    }
}
