package com.soen6441.battleship.services;

import com.soen6441.battleship.data.model.GamePlayer;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;
import com.soen6441.battleship.services.aiplayer.ProbabilityAIPlayer;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import io.reactivex.subjects.BehaviorSubject;
import org.junit.Test;

public class ProbabilityAITest {

    @Test
    public void testAI() {
        GamePlayer gamePlayer = new GamePlayer("Player", new GameGrid(8));

        try {
            gamePlayer.getGameGrid().placeShip(new Ship.Builder()
                    .setDirection(ShipDirection.HORIZONTAL)
                    .setStartCoordinates(1, 1)
                    .setEndCoordinates(6, 1)
                    .setLength(5)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }

        GameGrid aiGameGrid = new GameGrid(8);
        ProbabilityAIPlayer aiPlayer = new ProbabilityAIPlayer("AI Player", aiGameGrid, gamePlayer, coordinate -> {
            System.out.println(coordinate.getX() + " / " + coordinate.getY());
        });

        BehaviorSubject<Boolean> isMyTurn = BehaviorSubject.create();

        aiPlayer.setIsMyTurn(isMyTurn);

        isMyTurn.onNext(true);
    }
}
