package com.soen6441.battleship.data.model;

import com.soen6441.battleship.data.interfaces.IPlayer;
import io.reactivex.Observable;

/**
 * Contains the information regarding a playerviewmodel.
 *
 * */
public class ManualPlayer implements IPlayer {
    @Override
    public Observable<Grid> getGrid() {
        return null;
    }

    @Override
    public void hitEnemyBoard(int x, int y) {

    }
}
