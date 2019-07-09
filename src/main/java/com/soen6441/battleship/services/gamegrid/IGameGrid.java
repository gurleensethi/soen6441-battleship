package com.soen6441.battleship.services.gamegrid;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import io.reactivex.Observable;

import java.util.List;

public interface IGameGrid {
    /**
     * Places a ship on the grid.
     *
     * @param ship The ship to be placed
     *             TODO: Describe all the exceptions that will be thrown.
     * @throws Exception thrown if the ship cannot be placed or error in ship data.
     */
    void placeShip(Ship ship) throws Exception;

    /**
     * Raw grid data structure.
     *
     * @return Grid data structure contained withing {@link GameGrid}
     */
    Grid getGrid();

    /**
     * Get the list of all ship currently present on the Grid.
     *
     * @return The list of ships currently on {@link Grid}
     */
    List<Ship> getShips();

    /*
     * Mark a hit on a specific coordinate on the board.
     * */
    HitResult hit(int x, int y) throws CoordinatesOutOfBoundsException;

    /**
     * Get the grid as an observable. Updates will be triggered if there
     * is any change on the grid.
     *
     * @return Grid wrapped inside an RxObservable.
     */
    Observable<Grid> getGridAsObservable();

    /**
     * Checks if all the ships are destroyed on the board.
     *
     * @return boolean if all the ships are destroyed on the board.
     */
    boolean areAllShipsDestroyed();
}
