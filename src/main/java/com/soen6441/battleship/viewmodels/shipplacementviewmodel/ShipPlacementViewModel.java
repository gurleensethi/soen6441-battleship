package com.soen6441.battleship.viewmodels.shipplacementviewmodel;

import com.soen6441.battleship.data.interfaces.IPlayer;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.services.gamecontroller.IGameController;

import java.util.logging.Logger;

public class ShipPlacementViewModel implements IShipPlacementViewModel {
    private static final Logger logger = Logger.getLogger(ShipPlacementViewModel.class.getName());
    private IGameController gameController;
    private IPlayer player;

    public ShipPlacementViewModel(IGameController gameController) {
        this.gameController = gameController;
        this.player = gameController.createOrGetPlayer("player");
    }

    @Override
    public void placeShip(Ship ship) {
        try {
            player.getGameGrid().placeShip(ship);
        } catch (Exception e) {
            logger.warning("Unable to place ship!");
            logger.warning(e.getMessage());
        }
    }
}
