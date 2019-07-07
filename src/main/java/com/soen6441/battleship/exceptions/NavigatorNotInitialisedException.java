package com.soen6441.battleship.exceptions;

public class NavigatorNotInitialisedException extends RuntimeException {
    public NavigatorNotInitialisedException() {
        super("Navigator not initialised. Make sure to call Navigator.init before using it!");
    }
}
