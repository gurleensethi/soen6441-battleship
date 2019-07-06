package com.soen6441.battleship.data.model;


import com.soen6441.battleship.data.interfaces.IPlayer;
import io.reactivex.Observable;
import com.soen6441.battleship.common.Constants;

/**
 * Contains the information regarding a playerviewmodel.
 *
 * */
public class ManualPlayer implements IPlayer {


    private Grid grid = new Grid(Constants.gridSize);

    @Override
    public Observable<Grid> getGrid() {
        return null;
    }

    @Override
    public void hitEnemyBoard(int x, int y) {

    }
}
