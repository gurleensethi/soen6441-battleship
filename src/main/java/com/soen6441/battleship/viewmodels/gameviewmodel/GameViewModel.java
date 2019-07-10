package com.soen6441.battleship.viewmodels.gameviewmodel;

import com.soen6441.battleship.data.model.GameOverInfo;
import com.soen6441.battleship.services.gamecontroller.IGameController;
import com.soen6441.battleship.data.model.Grid;
import io.reactivex.Observable;

/**
 * The type Game view model: This class takes data from model and passes over to observer.
 * The view model takes data from this class and displays on GUI.
 */
public class GameViewModel implements IGameViewModel {
    /**
     * Instance of GameController.
     */
    private IGameController gameController;

    /**
     * Instantiates a new Game view model.
     *
     * @param gameController the game controller
     */
    public GameViewModel(IGameController gameController) {
        this.gameController = gameController;
    }

    /**
     * @return Player grid as an observable.
     */
    @Override
    public Observable<Grid> getPlayerGrid() {
        return gameController.createOrGetPlayer("player").getGameGrid().getGridAsObservable();
    }

    /**
     * @return Enemy grid as an observable.
     */
    @Override
    public Observable<Grid> getEnemyGrid() {
        return gameController.createOrGetPlayer("enemy").getGameGrid().getGridAsObservable();
    }

    @Override
    public void sendHit(int x, int y) {
        gameController.hit(x, y);
    }

    /**
     * @return Observable that fires when player turn changes.
     */
    @Override
    public Observable<String> playerTurnChange() {
        return gameController.turnChange();
    }

    /**
     * @return Observable that fires when game is over.
     */
    @Override
    public Observable<GameOverInfo> isGameOver() {
        return gameController.isGameOver();
    }
}
