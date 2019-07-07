package com.soen6441.battleship.data.interfaces;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import io.reactivex.Observable;

public interface IPlayer {
    GameGrid getGameGrid();
}
