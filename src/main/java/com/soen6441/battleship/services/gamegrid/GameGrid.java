package com.soen6441.battleship.services.gamegrid;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.enums.ShipDirection;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.exceptions.DirectionCoordinatesMismatchException;
import com.soen6441.battleship.exceptions.InvalidShipPlacementException;
import com.soen6441.battleship.utils.GridUtils;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The type Game grid.
 */
public class GameGrid implements IGameGrid {
    private final Logger logger = Logger.getLogger(GameGrid.class.getName());

    private final Grid grid;
    private final List<Ship> ships = new ArrayList<>();
    private final BehaviorSubject<Grid> gridBehaviorSubject = BehaviorSubject.create();

    public GameGrid(int gridSize) {
        this.grid = new Grid(gridSize);
        gridBehaviorSubject.onNext(this.grid);
        logger.info(() -> String.format("Grid created successfully: %s", grid));
    }

    @Override
    public Grid getGrid() {
        return grid;
    }

    @Override
    public List<Ship> getShips() {
        return ships;
    }

    @Override
    public HitResult hit(int x, int y) throws CoordinatesOutOfBoundsException {
        // Check if the coordinates are correct
        if (!isValidCell(x, y)) {
            throw new CoordinatesOutOfBoundsException();
        }

        CellState state = grid.getCellState(x, y);

        HitResult result;

        if (state == CellState.EMPTY_HIT || state == CellState.SHIP_WITH_HIT) {
            result = HitResult.ALREADY_HIT;
        } else if (state == CellState.SHIP) {
            // TODO: Move this logic to a ship service
            grid.updateCellStatus(x, y, CellState.SHIP_WITH_HIT);

            Ship shipToHit = grid.getCellInfo(x, y).getShip();
            shipToHit.setHits(shipToHit.getHits() + 1);

            if (shipToHit.isSunk()) {
                // TODO: Move this logic to a ship service
                if (shipToHit.getDirection() == ShipDirection.HORIZONTAL) {
                    for (int shipX = shipToHit.getStartX(); shipX <= shipToHit.getEndX(); shipX++) {
                        grid.updateCellStatus(shipX, shipToHit.getStartY(), CellState.DESTROYED_SHIP);
                    }
                } else {
                    for (int shipY = shipToHit.getStartY(); shipY <= shipToHit.getEndY(); shipY++) {
                        grid.updateCellStatus(shipToHit.getStartX(), shipY, CellState.DESTROYED_SHIP);
                    }
                }
            }

            result = HitResult.HIT;
        } else {
            grid.updateCellStatus(x, y, CellState.EMPTY_HIT);
            result = HitResult.MISS;
        }

        gridBehaviorSubject.onNext(this.grid);
        return result;
    }

    @Override
    public Observable<Grid> getGridAsObservable() {
        return gridBehaviorSubject;
    }

    @Override
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

        // Check if there are no other ships surrounding the placement of current ship
        checkPointValidityForShip(ship);

        ships.add(ship);

        if (ship.getDirection() == ShipDirection.HORIZONTAL) {
            for (int i = ship.getStartX(); i <= ship.getEndX(); i++) {
                grid.updateCellStatus(i, ship.getStartY(), CellState.SHIP);
                grid.setShipOnCell(i, ship.getStartY(), ship);
            }
        } else if (ship.getDirection() == ShipDirection.VERTICAL) {
            for (int i = ship.getStartY(); i <= ship.getEndY(); i++) {
                grid.updateCellStatus(ship.getStartX(), i, CellState.SHIP);
                grid.setShipOnCell(ship.getStartX(), i, ship);
            }
        }

        gridBehaviorSubject.onNext(this.grid);

        logger.info(String.format("Successfully placed ship on grid %s", ship));

        GridUtils.printGrid(this.grid);
    }


    /**
     * @param ship to be added on the grid
     * @throws InvalidShipPlacementException if ship cannot be placed because of invalid surroundings.
     */
    private void checkPointValidityForShip(Ship ship) throws InvalidShipPlacementException {
        logger.info("Checking Ship validity...");
        if (ship.getDirection() == ShipDirection.HORIZONTAL) {
            for (int i = ship.getStartX(); i < ship.getEndX(); i++) {
                checkValidAndThrow(i, ship.getStartY());
            }
        } else if (ship.getDirection() == ShipDirection.VERTICAL) {
            for (int i = ship.getStartY(); i < ship.getEndY(); i++) {
                checkValidAndThrow(ship.getStartX(), i);
            }
        }
        logger.info("Ship validity check complete!");
    }

    private void checkValidAndThrow(int x, int y) throws InvalidShipPlacementException {
        if (isValidCell(x, y) && (grid.getCellState(x, y) == CellState.SHIP)) {
            throw new InvalidShipPlacementException();
        }
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0
                && x <= grid.getGridSize()
                && y >= 0
                && y <= grid.getGridSize();
    }

    @Override
    public String toString() {
        return "GameGrid{" +
                "grid=" + grid +
                ", ships=" + ships +
                '}';
    }
}
