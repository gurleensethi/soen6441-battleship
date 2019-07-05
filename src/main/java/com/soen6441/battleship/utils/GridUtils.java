package com.soen6441.battleship.utils;

import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.enums.CellState;

import java.util.logging.Logger;

public class GridUtils {
    private static final Logger logger = Logger.getLogger(GridUtils.class.getName());
    private static final String EMPTY_CHAR = " - ";
    private static final String SHIP_CHAR = " \uD83D\uDEA2 ";
    private static final String HIT_CHAR = " ❌ ";

    public static void printGrid(Grid grid) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < grid.getGridSize(); i++) {
            for (int j = 0; j < grid.getGridSize(); j++) {
                CellState state = grid.getCoordinates()[i][j];

                switch (state) {
                    case EMPTY:
                        stringBuilder.append(EMPTY_CHAR);
                        break;
                    case SHIP:
                        stringBuilder.append(SHIP_CHAR);
                        break;
                    default:
                        break;
                }
            }
            stringBuilder.append("\n");
        }

        logger.info("Game Grid: \n" + stringBuilder.toString());
    }
}
