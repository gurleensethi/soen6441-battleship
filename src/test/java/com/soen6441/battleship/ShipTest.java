package com.soen6441.battleship;

import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShipTest {
    private Ship ship;

    @Before()
    public void setUpEach() {
        ship = new Ship.Builder()
                .setName("New Ship 1")
                .setUniqueId("123")
                .setStartCoordinates(1, 1)
                .setEndCoordinates(6, 1)
                .setDirection(ShipDirection.HORIZONTAL)
                .setLength(5)
                .build();
    }

    @Test()
    public void hitCountUpdates() {
        ship.setHits(ship.getHits() + 1);
        assertEquals(1, ship.getHits());
    }

    @Test()
    public void shipsSinksWhenHitsEqualsLength() {
        ship.setHits(5);
        assertTrue(ship.isSunk());
    }

    @Test()
    public void shipDoesNotSinkHitNotEqualsLength() {
        ship.setHits(4);
        assertFalse(ship.isSunk());
    }
}
