package com.soen6441.battleship.services.gamecontroller;

import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.data.model.GameOverInfo;
import io.reactivex.Observable;

/**
 * Interface:
 * <ul>
 *     <li>To create/get player object.
 *     <li>To hit selected coordinates.
 *     <li> Observe turns and Game Status.
 * </ul>
 *
 */


public interface IGameController {
    /**
     * To create/get player object.
     * @param playerName
     * @return
     */
    IPlayer createOrGetPlayer(String playerName);

    /**
     * <p>Gets the hit result from the coordinates and changes the turn as per result.
     *
     * @param x - x coordinate to hit on grid
     * @param y - y coordinate to hit on grid
     */
    void hit(int x, int y);

    /**
     * Observer method to change turns
     * @return - Observer object
     */
    Observable<String> turnChange();

    /**
     * Observer method to check if game is over
     * @return - Observer object
     */
    Observable<GameOverInfo> isGameOver();
}
