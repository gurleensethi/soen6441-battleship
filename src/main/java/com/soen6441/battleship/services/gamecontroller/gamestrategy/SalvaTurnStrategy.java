package com.soen6441.battleship.services.gamecontroller.gamestrategy;

import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.data.model.GamePlayer;
import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import com.soen6441.battleship.utils.GridUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SalvaTurnStrategy implements ITurnStrategy {
    private static final Logger logger = Logger.getLogger(SimpleTurnStrategy.class.getName());
    private GamePlayer player;
    private GamePlayer enemy;
    private GamePlayer currentPlayer;
    private int maxPlayerTurns = 5;
    private int maxEnemyTurns = 5;
    private int playerTurns = 5;
    private int enemyTurns = 5;
    private List<Coordinate> playerCoordinateHits = new ArrayList<>();

    public SalvaTurnStrategy(GamePlayer player, GamePlayer enemy) {
        this.player = player;
        this.enemy = enemy;
        currentPlayer = player;
    }

    @Override
    public GamePlayer getNextTurn(GamePlayer player, GamePlayer enemy, HitResult result) {
        if (this.player == null || this.enemy == null) {
            this.player = player;
            this.enemy = enemy;
            currentPlayer = player;
        }

        if (currentPlayer == player) {
            playerTurns--;
        } else {
            if (result != HitResult.ALREADY_HIT) {
                enemyTurns--;
            }
        }

        if (playerTurns == 0) {
            this.currentPlayer = enemy;
            playerTurns = player.getGameGrid().getUnSunkShips();
            enemyTurns = enemy.getGameGrid().getUnSunkShips();
        }

        if (enemyTurns == 0) {
            this.currentPlayer = player;
            enemyTurns = enemy.getGameGrid().getUnSunkShips();
            playerTurns = player.getGameGrid().getUnSunkShips();
        }

        return currentPlayer;
    }

    @Override
    public HitResult hit(GamePlayer playerToHit, Coordinate coordinate) throws CoordinatesOutOfBoundsException {
        // If manual player is playing
        if (enemy == playerToHit) {
            playerCoordinateHits.add(coordinate);

            HitResult peekHit = playerToHit.getGameGrid().peekHit(coordinate);

            if (peekHit != HitResult.ALREADY_HIT) {
                playerToHit.getGameGrid().updateCellState(coordinate, CellState.TO_BE_PLACED);
            }

            // Last turn for player
            if (playerTurns == 1) {
                for (Coordinate prevCoordinates : this.playerCoordinateHits) {
                    playerToHit.getGameGrid().hit(prevCoordinates.getX(), prevCoordinates.getY());
                }
                this.playerCoordinateHits.clear();
            }

            return HitResult.MISS;
        } else {
            return playerToHit.getGameGrid().hit(coordinate.getX(), coordinate.getY());
        }
    }

    private void switchCurrentPlayer() {
        logger.info(() -> " " + (currentPlayer == player));
        if (currentPlayer == player) {
            logger.info(() -> "Switching turn to enemy.");
            currentPlayer = enemy;
        } else {
            logger.info(() -> "Switching turn to player.");
            currentPlayer = player;
        }
    }
}
