package com.soen6441.battleship.data.model;

import com.soen6441.battleship.enums.CellState;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Represents the grid as a plane of xy-coordinates.
 * The origin (x=0, y=0) is kept at top left.
 * Bottom right coordinates are (x=gridSize-1, y=gridSize-1).
 */
public class Grid {
    private static final Logger logger = Logger.getLogger(Grid.class.getName());
    /**
     * Size of the grid.
     * For example if gridSize=8, the plane will be 8x8.
     */
    private final int gridSize;

    /**
     * 2-D array of all the individual cells inside the grid.
     */
    private final CellInfo[][] coordinates;

    /**
     * @param gridSize size of the grid. Should be greater than 0.
     */
    public Grid(int gridSize) {
        this.gridSize = gridSize;
        this.coordinates = new CellInfo[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.coordinates[i][j] = new CellInfo(CellState.EMPTY, null);
            }
        }
    }

    /**
     * @return the size of grid.
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * Get {@link CellState} of a particular x y coordinate.
     * Can throw {@link ArrayIndexOutOfBoundsException} if any of the coordinate
     * is out of bounds. So make sure to pass correct coordinate.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return {@link CellState} of cell at xy.
     */
    public CellState getCellState(int x, int y) {
        return this.coordinates[y][x].getState();
    }

    /**
     * Update the {@link CellState} of a particular x y coordinate.
     *
     * @param x     x-coordinate
     * @param y     y-coordinate
     * @param state new state of the cell.
     */
    public void updateCellStatus(int x, int y, CellState state) {
        coordinates[y][x].setState(state);
    }

    /**
     * Link a {@link Ship} to a particular x y coordinate.
     *
     * @param x    x-coordinate
     * @param y    y-coordinate
     * @param ship to be placed on the cell.
     */
    public void setShipOnCell(int x, int y, Ship ship) {
        coordinates[y][x].setShip(ship);
    }

    /**
     * @param x x-coordinate
     * @param y y-coordinate
     * @return {@link CellInfo} at particular x y coordinate.
     */
    public CellInfo getCellInfo(int x, int y) {
        return this.coordinates[y][x];
    }

    /**
     * @param coordinate coordinates
     * @return {@link CellInfo} at particular x y coordinate.
     */
    public CellInfo getCellInfo(Coordinate coordinate) {
        return this.coordinates[coordinate.getY()][coordinate.getX()];
    }

    @Override
    public String toString() {
        return "Grid{" +
                "gridSize=" + gridSize +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }
}
