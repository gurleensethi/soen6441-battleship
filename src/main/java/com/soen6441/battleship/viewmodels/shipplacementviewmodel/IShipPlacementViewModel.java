package com.soen6441.battleship.viewmodels.shipplacementviewmodel;

import com.soen6441.battleship.data.model.Ship;

/**
 * The interface Ship placement view model.
 */
public interface IShipPlacementViewModel {
    /**
     * Place ship: This method is and interface for the place ship method which validates the
     * data from controller and passes to model.
     *
     * @param ship the ship
     */
    void placeShip(Ship ship);
}
