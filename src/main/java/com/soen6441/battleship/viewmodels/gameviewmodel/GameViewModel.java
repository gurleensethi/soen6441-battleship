package com.soen6441.battleship.viewmodels.gameviewmodel;

import com.soen6441.battleship.services.gamecontroller.IGameController;
import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.data.model.Grid;
import io.reactivex.Observable;

public class GameViewModel implements IGameViewModel {
    private IGameController gameController;
    private IPlayer player;
    private IPlayer enemy;

    public GameViewModel(IGameController gameController) {
        this.gameController = gameController;
        player = gameController.createOrGetPlayer("player");
        enemy = gameController.createOrGetPlayer("enemy");
    }

    @Override
    public Observable<Grid> getPlayerGrid() {
        return player.getGameGrid().getGridAsObservable();
    }

    @Override
    public Observable<Grid> getEnemyGrid() {
        return enemy.getGameGrid().getGridAsObservable();
    }

    @Override
    public void sendHit(int x, int y) {
        gameController.hit(x, y);
    }
}
