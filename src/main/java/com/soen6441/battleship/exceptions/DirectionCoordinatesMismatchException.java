package com.soen6441.battleship.exceptions;

/**
 * The type Direction coordinates mismatch exception.
 */
public class DirectionCoordinatesMismatchException extends Exception {

    /**
     * Instantiates a new Direction coordinates mismatch exception.
     */
    public DirectionCoordinatesMismatchException() {
        super("Coordinates don't match the direction on ship!");
    }
}
