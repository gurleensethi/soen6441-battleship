package com.soen6441.battleship.enums;

public enum CellState {
    /*
     * When cell has nothing on it,
     * no hit or ship whatsoever.
     * */
    EMPTY,
    /*
     * When a cell has ship on it.
     * */
    SHIP,
    /**
     * Cell is hit, but there was no ship on it.
     */
    EMPTY_HIT,
    /**
     * Cell is hit, and there is ship on it.
     */
    SHIP_WITH_HIT,
    /**
     * Cell contains a ship that has been destroyed.
     */
    DESTROYED_SHIP,
}
