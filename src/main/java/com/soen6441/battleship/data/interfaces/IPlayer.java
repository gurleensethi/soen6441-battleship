package com.soen6441.battleship.data.interfaces;

import com.soen6441.battleship.data.model.Grid;
import io.reactivex.Observable;

public interface IPlayer {

    Observable<Grid> getGrid();

    void hitEnemyBoard(int x, int y);


}
