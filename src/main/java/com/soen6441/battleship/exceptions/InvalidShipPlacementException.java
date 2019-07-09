package com.soen6441.battleship.exceptions;

/**
 * The type Invalid ship placement exception.
 */
public class InvalidShipPlacementException extends Exception {

    /**
     * Instantiates a new Invalid ship placement exception.
     */
    public InvalidShipPlacementException() {
        super("Ship cannot be placed at these coordinate!");
    }
}
