package com.soen6441.battleship.services;

import com.soen6441.battleship.data.model.GameOverInfo;
import com.soen6441.battleship.data.model.GamePlayer;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.CellState;
import com.soen6441.battleship.services.gameconfig.GameConfig;
import com.soen6441.battleship.services.gamecontroller.GameController;
import com.soen6441.battleship.utils.GridUtils;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GameControllerTest {
    private GameController gameController;
    private Ship gameShip;

    @Before()
    public void setUp() {
        GameConfig.getsInstance().setSalvaVariation(false);

        try {
            Constructor<GameController> constructor = (Constructor<GameController>) GameController.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            gameController = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        gameShip = new Ship.Builder()
                .setStartCoordinates(7, 0)
                .setEndCoordinates(7, 0)
                .setLength(1)
                .build();

        GamePlayer enemyPlayer = gameController.createOrGetPlayer("enemy");

        enemyPlayer.getGameGrid().getShips().clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                enemyPlayer.getGameGrid().getGrid().setShipOnCell(i, j, null);
                enemyPlayer.getGameGrid().getGrid().updateCellStatus(i, j, CellState.EMPTY);
            }
        }
    }

    @Test()
    public void playerWinsTheGame() {
        Observable<GameOverInfo> gameOverInfo = gameController.isGameOver();
        TestObserver<GameOverInfo> testObserver = new TestObserver<>();
        gameOverInfo.subscribe(testObserver);

        placeShipAtTopOnEnemy();

        gameController.startGame();
        gameController.hit(7, 0);

        testObserver.assertValue(GameOverInfo::didPlayerWin);
    }

    @Test()
    public void gameQuitsWhenAPlayerWins() {
        Observable<GameOverInfo> gameOverInfo = gameController.isGameOver();
        TestObserver<GameOverInfo> testObserver = new TestObserver<>();
        gameOverInfo.subscribe(testObserver);

        placeShipAtTopOnEnemy();

        gameController.startGame();
        gameController.hit(7, 0);

        testObserver.assertValue(GameOverInfo::isGameOver);
    }

    @Test()
    public void turnChangesIfPlayerHitsWrongCell() {
        placeShipAtTopOnEnemy();

        Observable<String> turnChange = gameController.turnChange();
        TestObserver<String> testObserver = new TestObserver<>();
        turnChange.subscribe(testObserver);

        gameController.startGame();
        gameController.hit(5, 0);

        testObserver.assertValueAt(1, "enemy");
    }

    @Test()
    public void turnStaysSameWhenPlayerHitsACorrectCell() {
        placeShipAtTopOnEnemy();

        Observable<String> turnChange = gameController.turnChange();
        TestObserver<String> testObserver = new TestObserver<>();
        turnChange.subscribe(testObserver);

        gameController.startGame();
        gameController.hit(7, 0);

        testObserver.assertValueAt(1, "player");
    }

    private void placeShipAtTopOnEnemy() {
        GamePlayer enemyPlayer = gameController.createOrGetPlayer("enemy");
        enemyPlayer.getGameGrid().getShips().add(gameShip);
        enemyPlayer.getGameGrid().getGrid().setShipOnCell(7, 0, gameShip);
        enemyPlayer.getGameGrid().getGrid().updateCellStatus(7, 0, CellState.SHIP);
    }
}
