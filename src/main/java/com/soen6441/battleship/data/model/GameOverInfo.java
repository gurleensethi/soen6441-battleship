package com.soen6441.battleship.data.model;

public class GameOverInfo {
    private final boolean isGameOver;
    private final boolean didPlayerWin;

    public GameOverInfo(boolean isGameOver, boolean didPlayerWin) {
        this.isGameOver = isGameOver;
        this.didPlayerWin = didPlayerWin;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean didPlayerWin() {
        return didPlayerWin;
    }
}
