package com.soen6441.battleship.view.gui.scenes.gameplayscene;

import com.soen6441.battleship.data.model.CellInfo;
import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.data.model.Grid;
import com.soen6441.battleship.enums.CellState;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class GameGridPane extends StackPane implements EventHandler<ActionEvent> {
    private static final String GRID_BUTTON = "GridButton:";
    private final int gridSize;
    private final boolean isEnemyPlayer;

    private final GridPane buttonsGridPane = new GridPane();
    private final StackPane overlayPane = new StackPane();
    private IOnCoordinateHit onCoordinateHit;
    private Map<String, Button> buttons = new HashMap<>();
    private Map<String, Coordinate> buttonCoordinates = new HashMap<>();
    private Set<String> shipButtonsIds = new HashSet<>();

    GameGridPane(int gridSize, boolean isEnemyPlayer) {
        this.gridSize = gridSize;
        this.isEnemyPlayer = isEnemyPlayer;
        initStack();
        initGrid();
    }

    private void initStack() {
        this.getChildren().add(buttonsGridPane);

        overlayPane.setStyle("-fx-background-color: rgba(100, 100, 100, 0.3);");
        Text overlayText = new Text("Your Turn!");
        overlayText.setFont(new Font(20));
        overlayText.setFill(Color.WHITE);
        overlayPane.getChildren().add(overlayText);
    }

    private void initGrid() {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                String id = buildButtonId(x, y);

                Button button = new Button();
                button.setId(id);
                button.setPrefHeight(50);
                button.setPrefWidth(50);
                button.setOnAction(this);
                buttons.put(id, button);

                Coordinate coordinate = new Coordinate(x, y);
                buttonCoordinates.put(id, coordinate);

                // Add button to grid
                this.buttonsGridPane.add(button, x, y);
            }
        }
    }

    private String buildButtonId(int x, int y) {
        return GRID_BUTTON + x + " " + y;
    }

    void updateGrid(Grid grid) {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                CellInfo info = grid.getCellInfo(x, y);
                CellState cellState = info.getState();

                Button button = buttons.get(buildButtonId(x, y));

                switch (cellState) {
                    case EMPTY:
                        button.setText("");
                        break;
                    case SHIP:
                        button.setText("Ship");
                        break;
                    case EMPTY_HIT:
                        button.setText("Hit");
                        break;
                    case SHIP_WITH_HIT:
                        button.setText("ShipHit");
                        break;
                    case DESTROYED_SHIP:
                        button.setText("Destroyed");
                        break;
                }
            }
        }
    }

    void setOnCoordinateHit(IOnCoordinateHit onCoordinateHit) {
        this.onCoordinateHit = onCoordinateHit;
    }

    void setOverlayEnabled(boolean isEnabled) {
        if (isEnabled) {
            this.getChildren().add(overlayPane);
        } else {
            this.getChildren().remove(overlayPane);
        }
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getTarget() instanceof Button) {
            Button button = (Button) event.getTarget();
            String clickedButtonId = button.getId();

            // Check if the button clicked is button on grid
            if (clickedButtonId.startsWith(GRID_BUTTON)) {
                Coordinate coordinate = buttonCoordinates.get(clickedButtonId);
                if (this.onCoordinateHit != null) {
                    this.onCoordinateHit.onHit(coordinate);
                }
            }
        }
    }
}
