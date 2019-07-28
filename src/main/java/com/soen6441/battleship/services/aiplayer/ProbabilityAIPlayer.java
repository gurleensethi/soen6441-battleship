package com.soen6441.battleship.services.aiplayer;

import com.soen6441.battleship.data.interfaces.HitCallback;
import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.data.model.GamePlayer;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.enums.Direction;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.services.gameconfig.GameConfig;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import io.reactivex.Observable;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

public class ProbabilityAIPlayer extends GamePlayer implements IAIPlayer {
    private static final Logger logger = Logger.getLogger(ProbabilityAIPlayer.class.getName());
    /**
     * Manual player of the game.
     * AIPlayer hits the {@link com.soen6441.battleship.services.gamegrid.GameGrid} of this player.
     */
    private final GamePlayer player;

    private boolean isInTargetMode = false;

    private final HitCallback hitCallback;

    private final int cellDistributions[][];

    private final Set<Integer> destroyedShips = new HashSet<>();

    private final Stack<Coordinate> coordinatesToHit = new Stack<>();

    private Coordinate previousHitCoordinate;

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
        if (player.getGameGrid().areAllShipsDestroyed()) {
            return;
        }

        for (Ship ship : this.player.getGameGrid().getShips()) {
            if (ship.isSunk()) {
                this.destroyedShips.add(ship.getLength());
            }
        }

        // This loop continues until AI has found a coordinate to hit
        // or all ships of player are destroyed.
        // This loop is broken manually.
        logger.info("Thinking....");

        if (coordinatesToHit.empty()) {
            this.isInTargetMode = false;
        }

        if (isInTargetMode) {
            Coordinate cordsToHit = coordinatesToHit.pop();

            try {
                HitResult hitResult = this.player.getGameGrid().peekHit(cordsToHit);

                if (hitResult == HitResult.HIT) {
                    updateStackWithCoordinates(cordsToHit);
                }

                this.hitCallback.onHit(cordsToHit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            calculateDistributions();
            Coordinate cordsToHit = getHittableCoordinate();

            try {
                HitResult hitResult = this.player.getGameGrid().peekHit(cordsToHit);

                if (hitResult == HitResult.HIT) {
                    previousHitCoordinate = cordsToHit;
                    this.isInTargetMode = true;
                    updateStackWithCoordinates(cordsToHit);
                } else {
                    this.isInTargetMode = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.hitCallback.onHit(cordsToHit);
        }
    }

    private void updateStackWithCoordinates(Coordinate coordinate) {

        // TOP
        if (canHit(new Coordinate(coordinate.getX(), coordinate.getY() - 1))) {
            this.coordinatesToHit.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
        }

        // Bottom
        if (canHit(new Coordinate(coordinate.getX(), coordinate.getY() + 1))) {
            this.coordinatesToHit.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
        }

        // Left
        if (canHit(new Coordinate(coordinate.getX() - 1, coordinate.getY()))) {
            this.coordinatesToHit.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
        }

        // Right
        if (canHit(new Coordinate(coordinate.getX() + 1, coordinate.getY()))) {
            this.coordinatesToHit.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
        }
    }

    private boolean canHit(Coordinate coordinate) {
        int gridSize = GameConfig.getsInstance().getGridSize();

        if (this.coordinatesToHit.contains(coordinate)) {
            return false;
        }

        if (coordinate.getX() < 0
                || coordinate.getY() < 0
                || coordinate.getX() >= gridSize
                || coordinate.getY() >= gridSize) {
            return false;
        }

        CellState cellState = this.player.getGameGrid().getGrid().getCellInfo(coordinate).getState();

        if (cellState == CellState.EMPTY_HIT
                || cellState == CellState.SHIP_WITH_HIT
                || cellState == CellState.DESTROYED_SHIP) {
            return false;
        }

        return true;
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
                logger.info("------>>>>>" + shipLegth);
                for (int y = 0; y < this.cellDistributions.length; y++) {
                    for (int x = 0; x <= this.cellDistributions.length - shipLegth; x++) {

                        int horizontalWindow;

                        for (horizontalWindow = x; horizontalWindow < x + shipLegth; horizontalWindow++) {
                            Coordinate coordinate = new Coordinate(horizontalWindow, y);

                            CellState cellState = playerGameGrid.getGrid().getCellInfo(coordinate).getState();

                            if (cellState == CellState.EMPTY_HIT
                                    || cellState == CellState.SHIP_WITH_HIT
                                    || cellState == CellState.DESTROYED_SHIP) {
                                break;
                            }

                            this.cellDistributions[horizontalWindow][y]++;
                        }

                        if (horizontalWindow != (x + shipLegth)) {
                            for (int i = horizontalWindow - 1; i >= x; i--) {
                                this.cellDistributions[i][y]--;
                            }
                        }

                        int verticalWindow;

                        for (verticalWindow = x; verticalWindow < x + shipLegth; verticalWindow++) {
                            Coordinate coordinate = new Coordinate(y, verticalWindow);

                            CellState cellState = playerGameGrid.getGrid().getCellInfo(coordinate).getState();

                            if (cellState == CellState.EMPTY_HIT
                                    || cellState == CellState.SHIP_WITH_HIT
                                    || cellState == CellState.DESTROYED_SHIP) {
                                break;
                            }

                            this.cellDistributions[y][verticalWindow]++;
                        }

                        if (verticalWindow != (x + shipLegth)) {
                            for (int i = verticalWindow - 1; i >= x; i--) {
                                this.cellDistributions[y][i]--;
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

    private Coordinate getHittableCoordinate() {
        int largestValue = 0;
        Coordinate coordinate = new Coordinate(0, 0);

        for (int i = 0; i < this.cellDistributions.length; i++) {
            for (int j = 0; j < this.cellDistributions.length; j++) {
                int probabilityValue = this.cellDistributions[j][i];

                CellState cellState = this.player.getGameGrid().getGrid().getCellState(j, i);

                if (probabilityValue > largestValue
                        && (cellState != CellState.EMPTY_HIT && cellState != CellState.SHIP_WITH_HIT && cellState != CellState.DESTROYED_SHIP)) {
                    largestValue = probabilityValue;
                    coordinate = new Coordinate(j, i);
                }
            }
            System.out.println();
        }

        return coordinate;
    }
}
