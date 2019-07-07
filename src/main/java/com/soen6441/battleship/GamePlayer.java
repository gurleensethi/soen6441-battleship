package com.soen6441.battleship;

import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.services.gamegrid.GameGrid;

public class GamePlayer implements IPlayer {
    private String name;
    private GameGrid gameGrid;

    public GamePlayer(String name, GameGrid gameGrid) {
        this.gameGrid = gameGrid;
        this.name = name;
    }

    @Override
    public GameGrid getGameGrid() {
        return this.gameGrid;
    }
}
