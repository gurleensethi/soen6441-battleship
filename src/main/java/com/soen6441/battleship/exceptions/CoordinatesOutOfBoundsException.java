package com.soen6441.battleship.exceptions;

public class CoordinatesOutOfBoundsException extends Exception {

    public CoordinatesOutOfBoundsException() {
        super("Coordinates are out of bounds!");
    }
}
