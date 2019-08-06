package com.soen6441.battleship.services.gamecontroller;

import com.google.firebase.database.*;
import com.soen6441.battleship.data.model.*;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.services.aiplayer.ProbabilityAIPlayer;
import com.soen6441.battleship.services.boardgenerator.RandomShipPlacer;
import com.soen6441.battleship.services.gameconfig.GameConfig;
import com.soen6441.battleship.services.gamecontroller.gamestrategy.ITurnStrategy;
import com.soen6441.battleship.services.gamecontroller.gamestrategy.SalvaTurnStrategy;
import com.soen6441.battleship.services.gamecontroller.gamestrategy.SimpleTurnStrategy;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import com.soen6441.battleship.services.gameloader.GameLoader;
import com.soen6441.battleship.services.scorecalculator.ScoreCalculator;
import com.soen6441.battleship.utils.TimerUtil;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.Date;
import java.util.logging.Logger;

public class NetworkGameController implements IGameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());
    /**
     * GameController static instance
     */
    private static NetworkGameController sGameController;

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

    private ITurnStrategy turnStrategy;

    private String room = GameConfig.getsInstance().getRoomName();

    private GameConfig gameConfig = GameConfig.getsInstance();

    /**
     * Generates(if null) and returns GameController instance.
     *
     * @return The instance of GameController.
     */
    public static NetworkGameController getInstance() {
        if (sGameController == null) {
            sGameController = new NetworkGameController();
        }
        return sGameController;
    }

    /**
     * Constructor method to the class
     */
    private NetworkGameController() {
        currentPlayerName = "player";
        turnChangeBehaviourSubject.onNext(currentPlayerName);

        int gridSize = GameConfig.getsInstance().getGridSize();

        player = new GamePlayer("Player", new GameGrid(gridSize));
        enemy = new GamePlayer("Enemy", new GameGrid(gridSize));

        player.setIsMyTurn(playerTurnBehaviourSubject);
        enemy.setIsMyTurn(enemyTurnBehaviourSubject);
    }

    @Override
    public void startGame() {
        turnTimer.start();
        gameTimer.start();

        if (this.turnStrategy == null) {
            if (gameConfig.isSalvaVariation()) {
                this.turnStrategy = new SalvaTurnStrategy(this.player, this.enemy);
            } else {
                this.turnStrategy = new SimpleTurnStrategy();
            }
        }

        String fbPlayerName = GameConfig.getsInstance().getFBPlayerName();
        FirebaseDatabase.getInstance().getReference("games")
                .child(room)
                .child(fbPlayerName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            logger.info("Player grid update...");
                            logger.info("Player -> " + dataSnapshot.getKey());
                            Grid grid = dataSnapshot.getValue(Grid.class);
                            player.getGameGrid().updateGrid(grid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                    }
                });

        String enemyPlayerName = GameConfig.getsInstance().getFBEnemyName();
        FirebaseDatabase.getInstance().getReference("games")
                .child(room)
                .child(enemyPlayerName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            logger.info("Enemy grid update...");
                            logger.info("Enemy -> " + dataSnapshot.getKey());
                            Grid grid = dataSnapshot.getValue(Grid.class);
                            enemy.getGameGrid().updateGrid(grid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                    }
                });

        FirebaseDatabase.getInstance().getReference("games")
                .child(room)
                .child("playerTurn")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String playerName = dataSnapshot.getValue(String.class);
                        if (playerName.equals(GameConfig.getsInstance().getFBPlayerName())) {
                            currentPlayerName = "player";
                        } else {
                            currentPlayerName = "enemy";
                        }

                        turnChangeBehaviourSubject.onNext(currentPlayerName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
     * <p>Calls {@link NetworkGameController#handleIsGameOver()} to check if a player has won.
     *
     * @param x - x coordinate to hit on grid
     * @param y - y coordinate to hit on grid
     */
    @Override
    public void hit(int x, int y) {
        if (!currentPlayerName.equals("player")) {
            return;
        }

        // Return if game is over
        if (isGameOver) {
            return;
        }

        logger.info(() -> String.format("%s has sent a hit on x: %d, y: %d", this.currentPlayerName, x, y));

        try {


            HitResult result = enemy.getGameGrid().hit(x, y);

            updatePlayerAndEnemyGrid();

            //HitResult result = this.turnStrategy.hit(playerToHit, new Coordinate(x, y));

//            GamePlayer playerToSwitchTurnTo = this.turnStrategy.getNextTurn(player, enemy, result);
//
//            if (playerToSwitchTurnTo == player) {
//                currentPlayerName = "player";
//            } else {
//                currentPlayerName = "enemy";
//            }

            FirebaseDatabase.getInstance().getReference("games")
                    .child(room)
                    .child("playerTurn")
                    .setValueAsync(GameConfig.getsInstance().getFBEnemyName());

            turnChangeBehaviourSubject.onNext(this.currentPlayerName);
        } catch (CoordinatesOutOfBoundsException e) {
            e.printStackTrace();
        }

        //handleIsGameOver();

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
        turnTimer.reset();
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

    @Override
    public Long getFinalScore() {
        boolean didPlayerWin = this.enemy.getGameGrid().getUnSunkShips() == 0;
        return new ScoreCalculator().calculateScore(this.player.getTurnTimes(), didPlayerWin, enemy.getGameGrid().getUnSunkShips());
    }

    @Override
    public void saveGame() {
        GameControllerInfo offlineGameInfo = new GameControllerInfo();
        offlineGameInfo.setPlayerGrid(player.getGameGrid().getGrid());
        offlineGameInfo.setEnemyGrid(enemy.getGameGrid().getGrid());
        offlineGameInfo.setGridSize(GameConfig.getsInstance().getGridSize());
        offlineGameInfo.setStoreDate(new Date().getTime());
        offlineGameInfo.setCurrentTurn(this.currentPlayerName);
        offlineGameInfo.setSalva(GameConfig.getsInstance().isSalvaVariation());
        offlineGameInfo.setPlayerTurns(player.getTurnTimes());
        offlineGameInfo.setPlayerShips(player.getGameGrid().getShips());
        offlineGameInfo.setEnemyShips(enemy.getGameGrid().getShips());
        offlineGameInfo.setUnSunkPlayerShips(player.getGameGrid().getUnSunkShips());
        offlineGameInfo.setUnSunkEnemyShips(enemy.getGameGrid().getUnSunkShips());
        offlineGameInfo.setGameTime(this.gameTimer.getTime());
        offlineGameInfo.setTurnTime(this.turnTimer.getTime());
        if (turnStrategy instanceof SalvaTurnStrategy) {
            offlineGameInfo.setPlayerSalvaCoordinates(((SalvaTurnStrategy) turnStrategy).getPlayerCoordinateHits());
            offlineGameInfo.setPlayerSalvaTurns(((SalvaTurnStrategy) turnStrategy).getPlayerTurns());
        }
        GameLoader gameLoader = new GameLoader();
        gameLoader.saveGame(GameConfig.getsInstance().getPlayerName(), offlineGameInfo);
    }

    @Override
    public void loadOfflineGame() {
        GameLoader gameLoader = new GameLoader();
        GameControllerInfo offlineGameInfo = gameLoader.readSavedGame(GameConfig.getsInstance().getPlayerName());

        GameGrid playerGameGrid = new GameGrid(offlineGameInfo.getPlayerGrid());
        playerGameGrid.setShips(offlineGameInfo.getPlayerShips());
        this.player = new GamePlayer("player", playerGameGrid);
        this.player.setTurnTimes(offlineGameInfo.getPlayerTurns());

        GameGrid enemyGameGrid = new GameGrid(offlineGameInfo.getEnemyGrid());
        enemyGameGrid.setShips(offlineGameInfo.getEnemyShips());

        this.enemy = new ProbabilityAIPlayer("AI", enemyGameGrid, this.player, coordinate ->
                this.hit(coordinate.getX(), coordinate.getY()));

        this.currentPlayerName = offlineGameInfo.getCurrentTurn();

        this.player.setIsMyTurn(playerTurnBehaviourSubject);
        this.enemy.setIsMyTurn(enemyTurnBehaviourSubject);

        GameConfig.getsInstance().setSalvaVariation(offlineGameInfo.isSalva());

        if (GameConfig.getsInstance().isSalvaVariation()) {
            turnStrategy = new SalvaTurnStrategy(this.player, this.enemy);
            logger.info("Offline Salva Turns --> " + offlineGameInfo.getPlayerSalvaTurns());
            logger.info("Offline Salva Coordinates --> " + offlineGameInfo.getPlayerSalvaCoordinates());
            ((SalvaTurnStrategy) turnStrategy).setPlayerTurns(offlineGameInfo.getPlayerSalvaTurns());
            ((SalvaTurnStrategy) turnStrategy).setPlayerCoordinateHits(offlineGameInfo.getPlayerSalvaCoordinates());
        } else {
            turnStrategy = new SimpleTurnStrategy();
        }

        this.turnTimer.setTimeElapsed(offlineGameInfo.getTurnTime());
        this.gameTimer.setTimeElapsed(offlineGameInfo.getGameTime());
    }

    @Override
    public boolean isGameComplete() {
        return this.isGameOver;
    }

    private void updatePlayerAndEnemyGrid() {
        String roomName = GameConfig.getsInstance().getRoomName();
        String playerName = GameConfig.getsInstance().getFBPlayerName();
        String enemyName = GameConfig.getsInstance().getFBEnemyName();

        logger.info(playerName);
//        FirebaseDatabase.getInstance().getReference("games")
//                .child(roomName)
//                .child(playerName)
//                .setValueAsync(player.getGameGrid().getGrid());

        logger.info(enemyName);
        FirebaseDatabase.getInstance().getReference("games")
                .child(roomName)
                .child(enemyName)
                .setValueAsync(enemy.getGameGrid().getGrid());
    }
}
