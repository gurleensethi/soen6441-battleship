package com.soen6441.battleship.services.gamecontroller;

import com.soen6441.battleship.GamePlayer;
import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.enums.ShipDirection;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.Random;
import java.util.logging.Logger;

public class GameController implements IGameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());
    private static GameController sGameController;
    private String currentPlayerName;
    private BehaviorSubject<String> turnChangePublishSubject = BehaviorSubject.create();
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
        turnChangePublishSubject.onNext(currentPlayerName);
        player = new GamePlayer("Player", new GameGrid(8));
        enemy = new GamePlayer("Enemy", new GameGrid(8));
        try {
            enemy.getGameGrid().placeShip(new Ship.Builder()
                    .setLength(3)
                    .setStartCoordinates(0, 0)
                    .setEndCoordinates(0, 2)
                    .setDirection(ShipDirection.VERTICAL)
                    .build());

            enemy.getGameGrid().placeShip(new Ship.Builder()
                    .setLength(4)
                    .setStartCoordinates(1, 1)
                    .setEndCoordinates(4, 1)
                    .setDirection(ShipDirection.HORIZONTAL)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initPlayerTurnChangeListener();
    }

    private void initPlayerTurnChangeListener() {
        this.turnChangePublishSubject.subscribe(player -> {
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
        logger.info(String.format("%s has sent a hit on x: %d, y: %d", this.currentPlayerName, x, y));

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

            turnChangePublishSubject.onNext(this.currentPlayerName);
        } catch (CoordinatesOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Observable<String> turnChange() {
        return turnChangePublishSubject;
    }
}
