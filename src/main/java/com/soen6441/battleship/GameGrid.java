package com.soen6441.battleship;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.enums.ShipDirection;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.exceptions.DirectionCoordinatesMismatchException;
import com.soen6441.battleship.utils.GridUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameGrid {
    private final Logger logger = Logger.getLogger(GameGrid.class.getName());

    private final Grid grid;
    private final List<Ship> ships = new ArrayList<>();

    public GameGrid(int gridSize) {
        this.grid = new Grid(gridSize);
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
        if (ship.getStartX() < 0
                || ship.getStartY() < 0
                || ship.getEndX() > grid.getGridSize()
                || ship.getEndY() > grid.getGridSize()) {
            logger.severe("Ship Coordinates are out of bounds!");
            throw new CoordinatesOutOfBoundsException();
        }

        ships.add(ship);

        if (ship.getDirection() == ShipDirection.HORIZONTAL) {
            for (int i = ship.getStartX(); i < ship.getEndX(); i++) {
                grid.updateCellStatus(i, ship.getStartY(), CellState.SHIP);
            }
        } else if (ship.getDirection() == ShipDirection.VERTICAL) {
            for (int i = ship.getStartY(); i < ship.getEndY(); i++) {
                grid.updateCellStatus(ship.getStartX(), i, CellState.SHIP);
            }
        }

        logger.info(String.format("Successfully placed ship on grid %s", ship));

        GridUtils.printGrid(this.grid);
    }

    @Override
    public String toString() {
        return "GameGrid{" +
                "grid=" + grid +
                ", ships=" + ships +
                '}';
    }
}
