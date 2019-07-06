package com.soen6441.battleship.enums;

public enum HitResult {
    /**
     * Ship is hit successfully.
     */
    HIT,
    /**
     * Hit attempt was a miss.
     */
    MISS,
    /**
     * Hit attempt was already made for the coordinate.
     */
    ALREADY_HIT,
}
