package com.soen6441.battleship.viewmodels.gameviewmodel;

import com.soen6441.battleship.data.model.Grid;
import io.reactivex.Observable;

public interface IGameViewModel {
    Observable<Grid> getPlayerGrid();

    Observable<Grid> getEnemyGrid();

    void sendHit(int x, int y);

    Observable<String> playerTurnChange();
}
