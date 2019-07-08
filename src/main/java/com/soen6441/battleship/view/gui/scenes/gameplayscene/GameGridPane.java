package com.soen6441.battleship.view.gui.scenes.gameplayscene;

import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.data.model.Grid;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class GameGridPane extends GridPane implements EventHandler<ActionEvent> {
    private static final String GRID_BUTTON = "GridButton:";
    private final int gridSize;
    private final boolean isManualPlayer;

    private Map<String, Button> buttons = new HashMap<>();
    private Map<String, Coordinate> buttonCoordinates = new HashMap<>();
    private Set<String> shipButtonsIds = new HashSet<>();

    GameGridPane(int gridSize, boolean isManualPlayer) {
        this.gridSize = gridSize;
        this.isManualPlayer = isManualPlayer;
        initGrid();
    }

    private void initGrid() {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                String id = buildButtonId(x, y);

                Button button = new Button();
                button.setId(id);
                button.setPrefHeight(50);
                button.setPrefWidth(50);
                button.setDisable(this.isManualPlayer);
                button.setOnAction(this);
                buttons.put(id, button);

                Coordinate coordinate = new Coordinate(x, y);
                buttonCoordinates.put(id, coordinate);

                // Add button to grid
                add(button, x, y);
            }
        }
    }

    private String buildButtonId(int x, int y) {
        return GRID_BUTTON + x + " " + y;
    }

    public void updateGrid(Grid grid) {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {

            }
        }
    }

    @Override
    public void handle(ActionEvent event) {

    }
}
