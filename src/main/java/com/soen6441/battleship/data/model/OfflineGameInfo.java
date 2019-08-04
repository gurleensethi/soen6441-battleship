package com.soen6441.battleship.data.model;

import java.io.Serializable;

public class OfflineGameInfo implements Serializable {
    private String currentRouteName;
    private boolean isGameOver;

    public String getCurrentRouteName() {
        return currentRouteName;
    }

    public void setCurrentRouteName(String currentRouteName) {
        this.currentRouteName = currentRouteName;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
}
