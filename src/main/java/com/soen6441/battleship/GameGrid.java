package com.soen6441.battleship;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.exceptions.DirectionCoordinatesMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameGrid {
    private final Logger logger = Logger.getLogger("GameGrid");

    private final Grid grid;
    private final List<Ship> ships = new ArrayList<>();

    public GameGrid(int gridSize) {
        this.grid = new Grid(0, gridSize);
        logger.info("Grid created successfully: " + grid);
    }

    public Grid getGrid() {
        return grid;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void placeShip(Ship ship) throws Exception {
        checkNotNull(ship);
        logger.info(String.format("Placing ship on grid: %s", ship));

        // Check if direction of ship matches the coordinates
        if ((ship.getDirection() == ShipDirection.HORIZONTAL && ship.getStartY() != ship.getEndY())
                || (ship.getDirection() == ShipDirection.VERTICAL && ship.getStartX() != ship.getEndX())) {
            throw new DirectionCoordinatesMismatchException();
        }

        // Check if all coordinates passed lie on plane.
        if (ship.getStartX() < grid.getWidth()
                || ship.getStartY() < grid.getWidth()
                || ship.getEndX() > grid.getHeight()
                || ship.getEndY() > grid.getHeight()) {
            logger.severe("Ship Coordinates are out of bounds!");
            throw new CoordinatesOutOfBoundsException();
        }

        logger.info(String.format("Successfully placed ship on grid %s", ship));
        ships.add(ship);
    }

    @Override
    public String toString() {
        return "GameGrid{" +
                "grid=" + grid +
                ", ships=" + ships +
                '}';
    }
}
