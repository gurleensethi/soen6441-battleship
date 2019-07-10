package com.soen6441.battleship.services.gamecontroller;

import com.soen6441.battleship.data.model.GamePlayer;
import com.soen6441.battleship.data.model.GameOverInfo;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.services.aiplayer.AIPlayer;
import com.soen6441.battleship.services.aiplayer.IAIPlayer;
import com.soen6441.battleship.services.boardgenerator.RandomShipPlacer;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.logging.Logger;


/**
 * <p>GameController is the entity which drives the game.
 * Singleton class.
 *
 * <p>Major controls include:
 * <ul>
 * <li>Creating grids -  {@link com.soen6441.battleship.services.gamecontroller.GameController#GameController}
 * <li>Tracking hits - {@link com.soen6441.battleship.services.gamecontroller.GameController#initPlayerTurnChangeListener}
 * <li>Tracking turns - {@link com.soen6441.battleship.services.gamecontroller.GameController#hit}
 * <li>Handle game winner - {@link GameController#handleIsGameOver}
 * </ul>
 * <p>
 * Implements {@link com.soen6441.battleship.services.gamecontroller.IGameController} interface.
 */
public class GameController implements IGameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());
    /**
     * GameController static instance
     */
    private static GameController sGameController;

    /**
     * Player with active turn.
     */
    private String currentPlayerName;
    /**
     * Observer object to track turns
     */
    private BehaviorSubject<String> turnChangeBehaviourSubject = BehaviorSubject.create();
    private boolean isGameOver = false;
    /**
     * Observer object to track game status
     */
    private BehaviorSubject<GameOverInfo> isGameOverBehaviourSubject = BehaviorSubject.create();
    private GamePlayer player;
    private GamePlayer enemy;
    private IAIPlayer aiPlayer;

    /**
     * Generates(if null) and returns GameController instance.
     *
     * @return The instance of GameController.
     */
    public static GameController getInstance() {
        if (sGameController == null) {
            sGameController = new GameController();
        }
        return sGameController;
    }

    /**
     * Constructor method to the class
     */
    private GameController() {
        currentPlayerName = "player";
        turnChangeBehaviourSubject.onNext(currentPlayerName);

        player = new GamePlayer("Player", new GameGrid(8));
        enemy = new GamePlayer("Enemy", new GameGrid(8));
        aiPlayer = new AIPlayer(player);

        // Place random ships on board
        RandomShipPlacer randomShipPlacer = new RandomShipPlacer();
        randomShipPlacer.placeRandomShips(enemy.getGameGrid());

        initPlayerTurnChangeListener();
    }

    /**
     * Listener method for enemy to hit
     */
    private void initPlayerTurnChangeListener() {
        this.turnChangeBehaviourSubject.subscribe(player -> {
            if (player.equals("enemy")) {

            }
        });
    }

    /**
     * @return - true if it's players turn
     */
    private boolean isPlayerPlaying() {
        return currentPlayerName.equals("player");
    }

    /**
     * Sets {@link GameController#currentPlayerName} variable
     */
    private void nextPlayerTurn() {
        if (currentPlayerName.equals("player")) {
            currentPlayerName = "enemy";
        } else {
            currentPlayerName = "player";
        }
    }

    /**
     * Interface {@link IGameController} method
     *
     * @return enemy ot player object
     */
    @Override
    public GamePlayer createOrGetPlayer(String playerName) {
        if (playerName.equals("player")) {
            return player;
        } else {
            return enemy;
        }
    }

    /**
     * Interface method : {@link com.soen6441.battleship.services.gamecontroller.IGameController}
     * <p>Calls {@link GameController#handleIsGameOver()} to check if a player has won.
     *
     * @param x - x coordinate to hit on grid
     * @param y - y coordinate to hit on grid
     */

    @Override
    public void hit(int x, int y) {
        // Return if game is over
        if (isGameOver) {
            return;
        }

        logger.info(() -> String.format("%s has sent a hit on x: %d, y: %d", this.currentPlayerName, x, y));

        if (isPlayerPlaying()) {
            try {
                HitResult result = enemy.getGameGrid().hit(x, y);

                if (result == HitResult.MISS || result == HitResult.ALREADY_HIT) {
                    aiPlayer.takeHit();
                }

                turnChangeBehaviourSubject.onNext(this.currentPlayerName);
            } catch (CoordinatesOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        handleIsGameOver();
    }

    /**
     * Declares a winner by checking if all ships of either side are destroyed.
     */
    private void handleIsGameOver() {
        boolean areAllShipsOnEnemyHit = enemy.getGameGrid().areAllShipsDestroyed();
        boolean areAllShipsOnPlayerHit = player.getGameGrid().areAllShipsDestroyed();

        this.isGameOver = areAllShipsOnEnemyHit || areAllShipsOnPlayerHit;

        GameOverInfo gameOverInfo = new GameOverInfo(this.isGameOver, areAllShipsOnEnemyHit);
        this.isGameOverBehaviourSubject.onNext(gameOverInfo);
    }

    /**
     * Observer method to change turns
     *
     * @return - Observer object
     */
    @Override
    public Observable<String> turnChange() {
        return turnChangeBehaviourSubject;
    }

    /**
     * Observer method to check if game is over
     *
     * @return - Observer object
     */
    @Override
    public Observable<GameOverInfo> isGameOver() {
        return this.isGameOverBehaviourSubject;
    }
}
