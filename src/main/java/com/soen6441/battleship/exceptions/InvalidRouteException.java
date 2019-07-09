package com.soen6441.battleship.exceptions;

/**
 * The type Invalid route exception.
 */
public class InvalidRouteException extends RuntimeException {
    /**
     * Instantiates a new Invalid route exception.
     */
    public InvalidRouteException() {
        super("Passed route is invalid!");
    }
}
