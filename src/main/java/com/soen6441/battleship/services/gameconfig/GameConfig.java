package com.soen6441.battleship.services.gameconfig;

/**
 * Class type GameConfig configures the properties based on user selection.
 */

public class GameConfig {
    private static GameConfig sInstance;
    private String playerName = "Player";
    private boolean isSalvaVariation = false;
    private int gridSize = 10;

    private GameConfig() {
    }

    /**
     * Create GameConfig instance.
     * @return gameConfig instance.
     */
    public static GameConfig getsInstance() {
        if (sInstance == null) {
            sInstance = new GameConfig();
        }
        return sInstance;
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isSalvaVariation() {
        return isSalvaVariation;
    }

    public void setSalvaVariation(boolean salvaVariation) {
        isSalvaVariation = salvaVariation;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }
}
