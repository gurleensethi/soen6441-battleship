package com.soen6441.battleship.services.gamecontroller;

import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.data.model.GameOverInfo;
import io.reactivex.Observable;

public interface IGameController {
    IPlayer createOrGetPlayer(String playerName);

    void hit(int x, int y);

    Observable<String> turnChange();

    Observable<GameOverInfo> isGameOver();
}
