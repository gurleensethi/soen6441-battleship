package com.soen6441.battleship.services.aiplayer;

import com.soen6441.battleship.data.interfaces.HitCallback;
import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.data.model.GamePlayer;
import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.enums.Direction;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.services.gameconfig.GameConfig;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import io.reactivex.Observable;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class ProbabilityAIPlayer extends GamePlayer implements IAIPlayer {
    private static final Logger logger = Logger.getLogger(AIPlayer.class.getName());
    /**
     * Manual player of the game.
     * AIPlayer hits the {@link com.soen6441.battleship.services.gamegrid.GameGrid} of this player.
     */
    private final GamePlayer player;

    /**
     * Coordinates of the previous attempted hit.
     */
    private Coordinate previousCoordinates = null;

    private boolean wasPreviousHitSuccessful = false;

    private final HitCallback hitCallback;

    private final int cellDistributions[][];

    private final Set<Integer> destroyedShips = new HashSet<>();

    /**
     * Instantiates a new Game player.
     *
     * @param name     the name
     * @param gameGrid the game grid
     */
    public ProbabilityAIPlayer(String name, GameGrid gameGrid, GamePlayer otherPlayer, HitCallback hitCallback) {
        super(name, gameGrid);
        this.player = otherPlayer;
        this.hitCallback = hitCallback;
        int gridSize = GameConfig.getsInstance().getGridSize();
        this.cellDistributions = new int[gridSize][gridSize];
    }

    @Override
    public void setIsMyTurn(Observable<Boolean> isMyTurn) {
        super.setIsMyTurn(isMyTurn);

        this.isMyTurn.subscribe(turn -> {
            if (turn) {
                logger.info(() -> "AI has been give the turn!");
                takeHit();
            }
        });
    }

    /**
     * Take a hit on manual player's {@link com.soen6441.battleship.services.gamegrid.GameGrid} board.
     */
    @Override
    public void takeHit() {
        calculateDistributions();

        if (player.getGameGrid().areAllShipsDestroyed()) {
            return;
        }

        // This loop continues until AI has found a coordinate to hit
        // or all ships of player are destroyed.
        // This loop is broken manually.
        logger.info("Thinking....");
        boolean shouldBreak = false;

        Coordinate cordsToHit = null;
        if (!wasPreviousHitSuccessful) {
            cordsToHit = getRandomHitCords();
        } else {
            Direction previousAttemptedDirection = Direction.UP;

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
            previousCoordinates = cordsToHit;
            HitResult hitResult = player.getGameGrid().peekHit(cordsToHit);
            logger.info("AI is attempting hit on x: " + cordsToHit.getX() + " y: " + cordsToHit.getY());

            switch (hitResult) {
                case HIT: {
                    wasPreviousHitSuccessful = true;
                    break;
                }
                case MISS:
                case ALREADY_HIT: {
                    wasPreviousHitSuccessful = false;
                    break;
                }
            }

            this.hitCallback.onHit(cordsToHit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return random coordinates to hit.
     */
    private Coordinate getRandomHitCords() {
        Random random = new Random();
        return new Coordinate(random.nextInt(8), random.nextInt(8));
    }

    private void calculateDistributions() {
        resetDistributions();

        GameGrid playerGameGrid = this.player.getGameGrid();

        for (int shipLegth = 5; shipLegth > 0; shipLegth--) {
            if (!this.destroyedShips.contains(shipLegth)) {
                for (int y = 0; y < this.cellDistributions.length; y++) {
                    for (int x = 0; x <= this.cellDistributions.length - shipLegth; x++) {

                        int w;

                        for (w = x; w < x + shipLegth; w++) {
                            Coordinate coordinate = new Coordinate(w, y);

                            CellState cellState = playerGameGrid.getGrid().getCellInfo(coordinate).getState();

                            if (cellState == CellState.EMPTY_HIT
                                    || cellState == CellState.SHIP_WITH_HIT) {
                                break;
                            }

                            this.cellDistributions[w][y]++;
                        }

                        if (w != (x + shipLegth)) {
                            for (int i = w; i >= x; i++) {
                                this.cellDistributions[w][y]--;
                            }
                        }
                    }
                }
            }
        }

        printDistributions();
    }

    private void resetDistributions() {
        for (int i = 0; i < this.cellDistributions.length; i++) {
            for (int j = 0; j < this.cellDistributions.length; j++) {
                this.cellDistributions[i][j] = 0;
            }
        }
    }

    private void printDistributions() {
        for (int i = 0; i < this.cellDistributions.length; i++) {
            for (int j = 0; j < this.cellDistributions.length; j++) {
                System.out.print(this.cellDistributions[j][i] + "  ");
            }
            System.out.println();
        }
    }
}
