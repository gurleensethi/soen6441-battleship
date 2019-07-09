package com.soen6441.battleship.viewmodels.gameviewmodel;

import com.soen6441.battleship.data.model.GameOverInfo;
import com.soen6441.battleship.services.gamecontroller.IGameController;
import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.data.model.Grid;
import io.reactivex.Observable;

public class GameViewModel implements IGameViewModel {
    private IGameController gameController;

    public GameViewModel(IGameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public Observable<Grid> getPlayerGrid() {
        return gameController.createOrGetPlayer("player").getGameGrid().getGridAsObservable();
    }

    @Override
    public Observable<Grid> getEnemyGrid() {
        return gameController.createOrGetPlayer("enemy").getGameGrid().getGridAsObservable();
    }

    @Override
    public void sendHit(int x, int y) {
        gameController.hit(x, y);
    }

    @Override
    public Observable<String> playerTurnChange() {
        return gameController.turnChange();
    }

    @Override
    public Observable<GameOverInfo> isGameOver() {
        return gameController.isGameOver();
    }
}
