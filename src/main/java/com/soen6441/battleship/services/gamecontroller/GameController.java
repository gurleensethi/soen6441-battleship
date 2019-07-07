package com.soen6441.battleship.services.gamecontroller;

import com.soen6441.battleship.GamePlayer;
import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.services.gamegrid.GameGrid;

public class GameController implements IGameController {
    private static GameController sGameController;
    private String currentPlayerName;
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
        player = new GamePlayer("Player", new GameGrid(8));
        enemy = new GamePlayer("Enemy", new GameGrid(8));
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
        IPlayer playerToHit;

        if (isPlayerPlaying()) {
            playerToHit = enemy;
        } else {
            playerToHit = player;
        }

        try {
            HitResult result = playerToHit.getGameGrid().hit(x, y);

            if (result == HitResult.MISS) {
                currentPlayerName = "enemy";
            }

        } catch (CoordinatesOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
