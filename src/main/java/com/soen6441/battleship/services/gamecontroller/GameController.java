package com.soen6441.battleship.services.gamecontroller;

import com.soen6441.battleship.data.model.GamePlayer;
import com.soen6441.battleship.data.model.GameOverInfo;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.services.aiplayer.AIPlayer;
import com.soen6441.battleship.services.boardgenerator.RandomShipPlacer;
import com.soen6441.battleship.services.gamecontroller.gamestrategy.ITurnStrategy;
import com.soen6441.battleship.services.gamecontroller.gamestrategy.SalvaTurnStrategy;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import com.soen6441.battleship.utils.TimerUtil;
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
    private BehaviorSubject<Boolean> playerTurnBehaviourSubject = BehaviorSubject.create();
    private BehaviorSubject<Boolean> enemyTurnBehaviourSubject = BehaviorSubject.create();

    private TimerUtil turnTimer = new TimerUtil();
    private TimerUtil gameTimer = new TimerUtil();

    private ITurnStrategy gameStrategy;

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
        enemy = new AIPlayer("AI", new GameGrid(8), player, coordinate ->
                this.hit(coordinate.getX(), coordinate.getY()));

        player.setIsMyTurn(playerTurnBehaviourSubject);
        enemy.setIsMyTurn(enemyTurnBehaviourSubject);

        gameStrategy = new SalvaTurnStrategy();

        // Place random ships on board
        RandomShipPlacer randomShipPlacer = new RandomShipPlacer();
//        randomShipPlacer.placeRandomShips(player.getGameGrid());
        randomShipPlacer.placeRandomShips(enemy.getGameGrid());
    }

    @Override
    public void startGame() {
        turnTimer.start();
        gameTimer.start();
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

        try {
            long timeTaken = turnTimer.stop();

            if (currentPlayerName.equals("player")) {
                player.setTotalTimeTaken(timeTaken);
            } else {
                enemy.setTotalTimeTaken(timeTaken);
            }

            GamePlayer playerToHit = currentPlayerName.equals("player") ? enemy : player;

            HitResult result = playerToHit.getGameGrid().hit(x, y);

            GamePlayer playerToSwitchTurnTo = this.gameStrategy.getNextTurn(player, enemy, result);

            if (playerToSwitchTurnTo == player) {
                currentPlayerName = "player";
            } else {
                currentPlayerName = "enemy";
            }

            turnChangeBehaviourSubject.onNext(this.currentPlayerName);
        } catch (CoordinatesOutOfBoundsException e) {
            e.printStackTrace();
        }

        handleIsGameOver();

        if (!isGameOver) {
            notifyTurns();
        }
    }

    /**
     * Declares a winner by checking if all ships of either side are destroyed.
     */
    private void handleIsGameOver() {
        // Check if all ships of enemy or player are destroyed.
        boolean areAllShipsOnEnemyHit = enemy.getGameGrid().areAllShipsDestroyed();
        boolean areAllShipsOnPlayerHit = player.getGameGrid().areAllShipsDestroyed();

        this.isGameOver = areAllShipsOnEnemyHit || areAllShipsOnPlayerHit;

        GameOverInfo gameOverInfo = new GameOverInfo(this.isGameOver, areAllShipsOnEnemyHit);
        this.isGameOverBehaviourSubject.onNext(gameOverInfo);

        // Timers is stopped because we don't need it anymore as game
        // is over.
        if (isGameOver) {
            if (turnTimer.isRunning()) {
                turnTimer.stop();
            }

            if (gameTimer.isRunning()) {
                gameTimer.stop();
            }
        }
    }

    private void notifyTurns() {
        turnTimer.start();
        this.playerTurnBehaviourSubject.onNext(currentPlayerName.equals("player"));
        this.enemyTurnBehaviourSubject.onNext(currentPlayerName.equals("enemy"));
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

    @Override
    public Observable<Long> turnTimer() {
        return turnTimer.asObservable();
    }

    @Override
    public Observable<Long> gameTimer() {
        return gameTimer.asObservable();
    }
}
