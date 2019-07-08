package com.soen6441.battleship.exceptions;

public class InvalidRouteException extends RuntimeException {
    public InvalidRouteException() {
        super("Passed route is invalid!");
    }
}
