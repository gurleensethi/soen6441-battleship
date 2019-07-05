package com.soen6441.battleship.exceptions;

public class InvalidShipPlacementException extends Exception {

    public InvalidShipPlacementException() {
        super("Ship cannot be placed at these coordinate!");
    }
}
