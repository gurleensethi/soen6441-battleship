package com.soen6441.battleship.data.model;

import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.services.gamegrid.GameGrid;

/**
 * The type Game player is the class to instantiate the player type from which both human player
 * and AI player will take properties.
 */
public class GamePlayer implements IPlayer {
    private String name;
    private GameGrid gameGrid;

    /**
     * Instantiates a new Game player.
     *
     * @param name     the name
     * @param gameGrid the game grid
     */
    public GamePlayer(String name, GameGrid gameGrid) {
        this.gameGrid = gameGrid;
        this.name = name;
    }

    @Override
    public GameGrid getGameGrid() {
        return this.gameGrid;
    }
}
