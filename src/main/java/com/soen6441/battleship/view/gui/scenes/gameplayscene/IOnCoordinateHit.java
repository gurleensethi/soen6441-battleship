package com.soen6441.battleship.view.gui.scenes.gameplayscene;

import com.soen6441.battleship.data.model.Coordinate;

@FunctionalInterface()
public interface IOnCoordinateHit {
    void onHit(Coordinate coordinate);
}
