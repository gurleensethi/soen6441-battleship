package com.soen6441.battleship.data.model;

import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

public class Grid {
    private final int width;
    private final int height;
    private final CellState[][] coordinates;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.coordinates = new CellState[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.coordinates[i][j] = CellState.EMPTY;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CellState[][] getCoordinates() {
        return coordinates;
    }

    public void placeShip(Ship ship){
    }

    @Override
    public String toString() {
        return "Grid{" +
                "width=" + width +
                ", height=" + height +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }
}
