package com.soen6441.battleship.data.repository;

public class PlayerConnected {
    private final String displayName;
    private final String name;

    public PlayerConnected(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
