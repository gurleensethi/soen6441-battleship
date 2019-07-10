package com.soen6441.battleship.data.model;

import com.soen6441.battleship.services.gamegrid.GameGrid;

/**
 * The type Game player is the class to instantiate the player type from which both human player
 * and AI player will take properties.
 */
public class GamePlayer {
    private final String name;
    private final GameGrid gameGrid;

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

    public GameGrid getGameGrid() {
        return this.gameGrid;
    }

    public String getName() {
        return name;
    }
}
