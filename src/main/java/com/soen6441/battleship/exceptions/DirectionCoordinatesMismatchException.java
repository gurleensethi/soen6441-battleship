package com.soen6441.battleship.exceptions;

public class DirectionCoordinatesMismatchException extends Exception {

    public DirectionCoordinatesMismatchException() {
        super("Coordinates don't match the direction on ship!");
    }
}
