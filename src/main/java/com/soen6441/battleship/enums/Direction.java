package com.soen6441.battleship.enums;

public enum Direction {
    UP(0),
    DOWN(1),
    LEFT(2),
    RIGHT(3);

    public final int code;

    Direction(int code) {
        this.code = code;
    }

    public static Direction getFromCode(int code) {
        switch (code) {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.DOWN;
            case 2:
                return Direction.LEFT;
            default:
                return Direction.RIGHT;
        }
    }
}
