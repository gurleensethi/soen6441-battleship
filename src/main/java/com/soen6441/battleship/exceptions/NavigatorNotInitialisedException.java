package com.soen6441.battleship.exceptions;

/**
 * The type Navigator not initialised exception.
 */
public class NavigatorNotInitialisedException extends RuntimeException {
    /**
     * Instantiates a new Navigator not initialised exception.
     */
    public NavigatorNotInitialisedException() {
        super("Navigator not initialised. Make sure to call Navigator.init before using it!");
    }
}
