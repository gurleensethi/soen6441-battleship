package com.soen6441.battleship.data.model;

public class Grid {
    final private int width;
    final private int height;
    final public Coordinate[][] coordinates;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.coordinates = new Coordinate[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                coordinates[x][y] = new Coordinate(x, y);
            }
        }
    }
}
