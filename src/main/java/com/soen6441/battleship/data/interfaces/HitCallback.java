package com.soen6441.battleship.data.interfaces;

import com.soen6441.battleship.data.model.Coordinate;

@FunctionalInterface
public interface HitCallback {
    void onHit(Coordinate coordinate);
}
