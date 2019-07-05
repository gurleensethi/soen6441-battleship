package com.soen6441.battleship.viewmodels.playerviewmodel;

import com.soen6441.battleship.data.model.Grid;
import io.reactivex.Observable;

public interface IPlayerViewModel {
    Observable<Grid> getGrid();

    void hitEnemy(int x, int y);
}
