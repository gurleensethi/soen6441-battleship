package com.soen6441.battleship.data.interfaces;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.services.gamegrid.GameGrid;
import com.soen6441.battleship.services.gamegrid.IGameGrid;
import io.reactivex.Observable;

public interface IPlayer {
    IGameGrid getGameGrid();
}
