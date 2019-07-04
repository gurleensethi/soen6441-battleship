package com.soen6441.battleship.data.model;

public class Coordinate {
    final int x;
    final int y;
    private boolean isHit = false;
    private boolean hasShip = false;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public boolean isHasShip() {
        return hasShip;
    }

    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }
}
