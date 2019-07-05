package com.soen6441.battleship.data.model;

import com.soen6441.battleship.enums.CellState;

import java.util.Arrays;
import java.util.logging.Logger;

public class Grid {
    private static final Logger logger = Logger.getLogger(Grid.class.getName());
    private final int gridSize;
    private final CellState[][] coordinates;

    public Grid(int gridSize) {
        this.gridSize = gridSize;
        this.coordinates = new CellState[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.coordinates[i][j] = CellState.EMPTY;
            }
        }
    }

    public int getGridSize() {
        return gridSize;
    }

    public CellState getCellState(int x, int y) {
        return this.coordinates[y][x];
    }

    public void updateCellStatus(int x, int y, CellState newStatus) {
        coordinates[y][x] = newStatus;
    }

    @Override
    public String toString() {
        return "Grid{" +
                "gridSize=" +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }
}
