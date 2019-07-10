package com.soen6441.battleship.services.aiplayer;

import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.data.model.GamePlayer;
import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.enums.Direction;
import com.soen6441.battleship.enums.HitResult;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Player that takes turns automatically.
 * A very naive AI, that hits a coordinate on random.
 * If a hit is found it tries to hit the nearby coordinates.
 */
public class AIPlayer implements IAIPlayer {
    private static final Logger logger = Logger.getLogger(AIPlayer.class.getName());
    /**
     * Manual player of the game.
     * AIPlayer hits the {@link com.soen6441.battleship.services.gamegrid.GameGrid} of this player.
     */
    private GamePlayer player;

    /**
     * Coordinates of the previous attempted hit.
     */
    private Coordinate previousCoordinates = null;

    public AIPlayer(GamePlayer player) {
        this.player = player;
    }

    /**
     * Take a hit on manual player's {@link com.soen6441.battleship.services.gamegrid.GameGrid} board.
     */
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
                Direction previousAttemptedDirection = Direction.UP;

                while (true) {
                    cordsToHit = getUpdatedCoordinatesFromDirection(previousAttemptedDirection, previousCoordinates);

                    CellState cellState = player.getGameGrid().getGrid().getCellState(cordsToHit.getX(), cordsToHit.getY());

                    if (cordsToHit.getX() < 8
                            && cordsToHit.getX() >= 0
                            && cordsToHit.getY() < 8
                            && cordsToHit.getY() >= 0
                            && cellState != CellState.EMPTY_HIT
                            && cellState != CellState.SHIP_WITH_HIT
                            && cellState != CellState.DESTROYED_SHIP
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
                        shouldBreak = false;
                        wasPreviousHitSuccessful = true;
                        break;
                    }
                    case MISS: {
                        shouldBreak = true;
                        wasPreviousHitSuccessful = false;
                        break;
                    }
                    case ALREADY_HIT: {
                        shouldBreak = false;
                        wasPreviousHitSuccessful = false;
                        break;
                    }
                }

            } catch (Exception e) {
                shouldBreak = false;
                e.printStackTrace();
            }

            if (shouldBreak) {
                break;
            }
        }
    }

    /**
     * @return random coordinates to hit.
     */
    private Coordinate getRandomHitCords() {
        Random random = new Random();
        return new Coordinate(random.nextInt(8), random.nextInt(8));
    }

    /**
     * Maps the to a direction to the coordinate, i.e. changes the coordinate to represent
     * a single value move in a particular direction.
     *
     * @param direction  to move towards.
     * @param coordinate to update.
     * @return Updated Coordinates.
     */
    private Coordinate getUpdatedCoordinatesFromDirection(Direction direction, Coordinate coordinate) {
        switch (direction) {
            case UP:
                return new Coordinate(coordinate.getX(), coordinate.getY() - 1);
            case DOWN:
                return new Coordinate(coordinate.getX(), coordinate.getY() + 1);
            case LEFT:
                return new Coordinate(coordinate.getX() - 1, coordinate.getY());
            case RIGHT:
                return new Coordinate(coordinate.getX() + 1, coordinate.getY());
            default:
                return null;
        }
    }
}
