package com.soen6441.battleship.data.gridgame;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.data.model.Ship;

import java.util.List;

public interface IGridGame {
    void placeShip(Ship ship) throws Exception;

    Grid getGrid();

    List<Ship> getShips();
}
