package com.soen6441.battleship.utils;

import com.soen6441.battleship.data.model.CellInfo;
import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.enums.ShipDirection;
import com.soen6441.battleship.services.gamegrid.IGameGrid;
import com.soen6441.battleship.data.model.Ship;

import java.util.Random;
import java.util.logging.Logger;

public class BoardGeneratorUtil {
    private static final Logger logger = Logger.getLogger(BoardGeneratorUtil.class.getName());
    private boolean enemyTurn = false;

    public void placeRandomShips(IGameGrid gameGrid) {
        int shipLength = 5;

        Random random = new Random();

        while (shipLength > 0) {

            while (true) {
                int startXCord = random.nextInt(8);
                int startYCord = random.nextInt(8);
                boolean isVertical = random.nextBoolean();
                ShipDirection shipDirection = isVertical ? ShipDirection.VERTICAL : ShipDirection.HORIZONTAL;

                int endXCord = startXCord;
                int endYCord = startYCord;

                if (shipDirection == ShipDirection.VERTICAL) {
                    endYCord += shipLength - 1;
                } else {
                    endXCord += shipLength - 1;
                }

                Ship ship = new Ship.Builder()
                        .setName("Ship:" + shipLength)
                        .setStartCoordinates(startXCord, startYCord)
                        .setEndCoordinates(endXCord, endYCord)
                        .setLength(shipLength)
                        .setDirection(shipDirection)
                        .build();

                try {
                    gameGrid.placeShip(ship);
                    shipLength--;
                    break;
                } catch (Exception e) {
                    logger.warning(() -> "Wrong coordinates. Trying again");
                }
            }
        }

        logger.info(() -> "Enemy ship placement successfully!");
    }
}
