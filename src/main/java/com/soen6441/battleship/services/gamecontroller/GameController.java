package com.soen6441.battleship.services.gamecontroller;

import com.soen6441.battleship.GamePlayer;
import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.data.model.GameOverInfo;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.enums.ShipDirection;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import com.soen6441.battleship.utils.BoardGeneratorUtil;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.Random;
import java.util.logging.Logger;

public class GameController implements IGameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());
    private static GameController sGameController;
    private String currentPlayerName;
    private BehaviorSubject<String> turnChangeBehaviourSubject = BehaviorSubject.create();
    private boolean isGameOver = false;
    private BehaviorSubject<GameOverInfo> isGameOverBehaviourSubject = BehaviorSubject.create();
    private IPlayer player;
    private IPlayer enemy;

    public static GameController getInstance() {
        if (sGameController == null) {
            sGameController = new GameController();
        }
        return sGameController;
    }

    private GameController() {
        currentPlayerName = "player";
        turnChangeBehaviourSubject.onNext(currentPlayerName);

        player = new GamePlayer("Player", new GameGrid(8));
        enemy = new GamePlayer("Enemy", new GameGrid(8));

        // Place random ships on board
        BoardGeneratorUtil boardGeneratorUtil = new BoardGeneratorUtil();
        boardGeneratorUtil.placeRandomShips(enemy.getGameGrid());

        initPlayerTurnChangeListener();
    }

    private void initPlayerTurnChangeListener() {
        this.turnChangeBehaviourSubject.subscribe(player -> {
            if (player.equals("enemy")) {
                Random random = new Random();
                int x = random.nextInt(8);
                int y = random.nextInt(8);
                this.hit(x, y);
            }
        });
    }

    private boolean isPlayerPlaying() {
        return currentPlayerName.equals("player");
    }

    private void nextPlayerTurn() {
        if (currentPlayerName.equals("player")) {
            currentPlayerName = "enemy";
        } else {
            currentPlayerName = "player";
        }
    }

    @Override
    public IPlayer createOrGetPlayer(String playerName) {
        if (playerName.equals("player")) {
            return player;
        } else {
            return enemy;
        }
    }

    @Override
    public void hit(int x, int y) {
        // Return if game is over
        if (isGameOver) {
            return;
        }

        logger.info(() -> String.format("%s has sent a hit on x: %d, y: %d", this.currentPlayerName, x, y));

        IPlayer playerToHit;

        if (isPlayerPlaying()) {
            playerToHit = enemy;
        } else {
            playerToHit = player;
        }

        try {
            HitResult result = playerToHit.getGameGrid().hit(x, y);

            if (result == HitResult.MISS || result == HitResult.ALREADY_HIT) {
                nextPlayerTurn();
            }

            turnChangeBehaviourSubject.onNext(this.currentPlayerName);
        } catch (CoordinatesOutOfBoundsException e) {
            e.printStackTrace();
        }

        handleIsGameOver();
    }

    private void handleIsGameOver() {
        boolean areAllShipsOnEnemyHit = enemy.getGameGrid().areAllShipsDestroyed();
        boolean areAllShipsOnPlayerHit = enemy.getGameGrid().areAllShipsDestroyed();
        this.isGameOver = areAllShipsOnEnemyHit || areAllShipsOnPlayerHit;

        GameOverInfo gameOverInfo = new GameOverInfo(this.isGameOver, areAllShipsOnPlayerHit);
        this.isGameOverBehaviourSubject.onNext(gameOverInfo);
    }

    @Override
    public Observable<String> turnChange() {
        return turnChangeBehaviourSubject;
    }

    @Override
    public Observable<GameOverInfo> isGameOver() {
        return this.isGameOverBehaviourSubject;
    }
}
