package com.soen6441.battleship.viewmodels.gameviewmodel;

import com.soen6441.battleship.data.model.GameOverInfo;
import com.soen6441.battleship.data.model.Grid;
import io.reactivex.Observable;

/**
 * The interface Game view model: is the interface for Gameview Model to take data from data.
 */
public interface IGameViewModel {
    /**
     * Gets player grid.
     *
     * @return the player grid
     */
    Observable<Grid> getPlayerGrid();

    /**
     * Gets enemy grid.
     *
     * @return the enemy grid
     */
    Observable<Grid> getEnemyGrid();

    /**
     * Send hit.
     *
     * @param x the x
     * @param y the y
     */
    void sendHit(int x, int y);

    /**
     * Player turn change observable.
     *
     * @return the observable
     */
    Observable<String> playerTurnChange();

    /**
     * Is game over observable.
     *
     * @return the observable
     */
    Observable<GameOverInfo> isGameOver();
}
