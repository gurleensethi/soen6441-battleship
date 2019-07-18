package com.soen6441.battleship.services.gamecontroller.gamestrategy;

import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.data.model.GameOverInfo;
import com.soen6441.battleship.data.model.GamePlayer;
import com.soen6441.battleship.enums.HitResult;
import com.soen6441.battleship.exceptions.CoordinatesOutOfBoundsException;
import io.reactivex.Observable;


public interface ITurnStrategy {
    /**
     * Get turn of next player, depending on hit made.
     *
     * @param result Result of previous hit
     * @return Player to which the turn should be switched.
     */
    GamePlayer getNextTurn(GamePlayer player, GamePlayer enemy, HitResult result);

    HitResult hit(GamePlayer player, Coordinate coordinate) throws CoordinatesOutOfBoundsException;
}
