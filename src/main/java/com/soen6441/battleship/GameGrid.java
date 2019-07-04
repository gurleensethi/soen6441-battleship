package com.soen6441.battleship;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.data.model.Ship;

import java.util.ArrayList;
import java.util.List;

public class GameGrid {
    static class Builder {
        private int width;
        private int height;
        private List<Ship> ships = new ArrayList<>();

        public Builder setDimensions(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public GameGrid build() {
            return new GameGrid(this);
        }

        public Builder addShip(Ship ship) {
            ships.add(ship);
            return this;
        }

        public Builder addShips(List<Ship> ships) {
            this.ships.addAll(ships);
            return this;
        }
    }

    private Grid grid;
    private List<Ship> ships;

    public GameGrid(Builder builder) {
        grid = new Grid(builder.width, builder.height);
    }

    private void initShips(List<Ship> ships) {

    }

    public void addHit(int x, int y) {
        this.grid.coordinates[x][y].setHasShip(true);
    }

    public boolean hasAlreadyHit(int x, int y) {
        return this.grid.coordinates[x][y].isHit();
    }
}
