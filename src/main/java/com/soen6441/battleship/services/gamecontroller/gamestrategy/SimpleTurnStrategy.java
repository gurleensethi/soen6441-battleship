package com.soen6441.battleship.services.gamecontroller.gamestrategy;

import com.soen6441.battleship.data.model.GamePlayer;
import com.soen6441.battleship.enums.HitResult;

import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleTurnStrategy implements ITurnStrategy {
    private static final Logger logger = Logger.getLogger(SimpleTurnStrategy.class.getName());
    private GamePlayer player;
    private GamePlayer enemy;
    private GamePlayer currentPlayer;

    public SimpleTurnStrategy() {
    }

    @Override
    public GamePlayer getNextTurn(GamePlayer player, GamePlayer enemy, HitResult result) {
        if (this.player == null || this.enemy == null) {
            this.player = player;
            this.enemy = enemy;
            currentPlayer = player;
        }

        if (!(result == HitResult.ALREADY_HIT && currentPlayer == enemy)
                && (result == HitResult.MISS || result == HitResult.ALREADY_HIT)) {
            switchCurrentPlayer();
        }

        return currentPlayer;
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
