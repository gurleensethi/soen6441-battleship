package com.soen6441.battleship.services.aiplayer;

import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.enums.Direction;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import javafx.scene.control.Cell;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class AIPlayer implements IAIPlayer {
    private static final Logger logger = Logger.getLogger(AIPlayer.class.getName());
    private IPlayer player;
    private Coordinate previousCoordinates = null;
    private Direction previousAttemptedDirection;

    public AIPlayer(IPlayer player) {
        this.player = player;
    }

    @Override
    public void takeHit() {
        boolean wasPreviousHitSuccessful = false;

        // This loop continues until AI has found a coordinate to hit
        // or all ships of player are destroyed.
        // This loop is broken manually.
        while (!player.getGameGrid().areAllShipsDestroyed()) {
            logger.info("Thinking....");
            boolean shouldBreak = false;

            Coordinate cordsToHit = null;
            if (!wasPreviousHitSuccessful) {
                cordsToHit = getRandomHitCords();
            } else {
                previousAttemptedDirection = Direction.UP;

                while (true) {
                    switch (previousAttemptedDirection) {
                        case UP:
                            cordsToHit = new Coordinate(previousCoordinates.getX(), previousCoordinates.getY() - 1);
                            break;
                        case DOWN:
                            cordsToHit = new Coordinate(previousCoordinates.getX(), previousCoordinates.getY() + 1);
                            break;
                        case LEFT:
                            cordsToHit = new Coordinate(previousCoordinates.getX() - 1, previousCoordinates.getY());
                            break;
                        case RIGHT:
                            cordsToHit = new Coordinate(previousCoordinates.getX() + 1, previousCoordinates.getY());
                            break;
                    }

                    if (cordsToHit.getX() < 8
                            && cordsToHit.getX() >= 0
                            && cordsToHit.getY() < 8
                            && cordsToHit.getY() >= 0
                            && player.getGameGrid().getGrid().getCellState(cordsToHit.getX(), cordsToHit.getY()) != CellState.EMPTY_HIT
                            && player.getGameGrid().getGrid().getCellState(cordsToHit.getX(), cordsToHit.getY()) != CellState.SHIP_WITH_HIT
                            && player.getGameGrid().getGrid().getCellState(cordsToHit.getX(), cordsToHit.getY()) != CellState.DESTROYED_SHIP
                    ) {
                        break;
                    } else {
                        if (previousAttemptedDirection == Direction.RIGHT) {
                            cordsToHit = getRandomHitCords();
                            break;
                        }
                        previousAttemptedDirection = Direction.getFromCode(previousAttemptedDirection.code + 1);
                    }
                }
            }

            try {
                HitResult hitResult = player.getGameGrid().hit(cordsToHit.getX(), cordsToHit.getY());
                logger.info("AI is attempting hit on x: " + cordsToHit.getX() + " y: " + cordsToHit.getY());

                switch (hitResult) {
                    case HIT: {
                        previousCoordinates = cordsToHit;
                        wasPreviousHitSuccessful = true;
                        break;
                    }
                    case MISS: {
                        shouldBreak = true;
                        wasPreviousHitSuccessful = false;
                        break;
                    }
                    case ALREADY_HIT: {
                        wasPreviousHitSuccessful = false;
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (shouldBreak) {
                break;
            }
        }
    }

    private Coordinate getRandomHitCords() {
        Random random = new Random();
        return new Coordinate(random.nextInt(8), random.nextInt(8));
    }
}
