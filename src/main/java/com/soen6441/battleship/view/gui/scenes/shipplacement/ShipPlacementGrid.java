package com.soen6441.battleship.view.gui.scenes.shipplacement;

import com.soen6441.battleship.data.model.Coordinate;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ShipPlacementGrid extends GridPane implements EventHandler<ActionEvent> {
    private static final Logger logger = Logger.getLogger(ShipPlacementGrid.class.getName());
    private static final String GRID_BUTTON = "GridButton:";
    private static final int MAX_SHIPS = 5;

    private int numOfShipsPlaced = 0;
    private final int gridSize;
    private Map<String, Button> buttons = new HashMap<>();
    private Map<String, Coordinate> buttonCoordinates = new HashMap<>();
    private Set<String> shipButtonsIds = new HashSet<>();
    private boolean isSelectingShip = false;
    private Coordinate prevSelectedBtnCoordinate;
    private PublishSubject<Boolean> isSelectingShipSubject = PublishSubject.create();
    private PublishSubject<Integer> numShipPlacedSubject = PublishSubject.create();

    public ShipPlacementGrid(int gridSize) {
        this.gridSize = gridSize;
        isSelectingShipSubject.onNext(this.isSelectingShip);
        numShipPlacedSubject.onNext(this.numOfShipsPlaced);
        initUI();
    }

    public Observable<Boolean> getShipSelectionObservable() {
        return this.isSelectingShipSubject;
    }

    public Observable<Integer> getSelectedShipCountObservable() {
        return this.numShipPlacedSubject;
    }

    private void initUI() {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                String id = buildButtonId(x, y);

                Button button = new Button();
                button.setId(id);
                button.setText(x + ", " + y);
                button.setPrefHeight(50);
                button.setPrefWidth(50);
                button.setOnAction(this);
                buttons.put(id, button);

                Coordinate coordinate = new Coordinate(x, y);
                buttonCoordinates.put(id, coordinate);

                // Add button to grid
                add(button, x, y);
            }
        }
    }

    private void updateUI() {
        if (numOfShipsPlaced < MAX_SHIPS) {
            // Disable all the buttons that have been selected for a ship.
            buttons.forEach((buttonId, button) -> button.setDisable(shipButtonsIds.contains(buttonId)));
        } else {
            buttons.forEach((buttonId, button) -> button.setDisable(true));
        }
    }


    @Override
    public void handle(ActionEvent event) {
        if (event.getTarget() instanceof Button) {
            final Button clickedButton = (Button) event.getTarget();
            final String clickedButtonId = clickedButton.getId();

            // Check if the button is grid button
            if (clickedButtonId.startsWith(GRID_BUTTON)) {
                if (!isSelectingShip) {
                    final Coordinate clickedButtonCoordinate = buttonCoordinates.get(clickedButtonId);
                    prevSelectedBtnCoordinate = clickedButtonCoordinate;
                    logger.info("User selected initial coordinates: " + clickedButtonCoordinate.toString());

                    disableAllButtons();
                    enableButtonsOnAxisOfCoordinate(clickedButtonCoordinate);

                    isSelectingShip = true;
                } else {
                    final Coordinate coordinate = buttonCoordinates.get(clickedButtonId);
                    logger.info("User selected final coordinates: " + coordinate.toString());

                    // Ship is only of size 1
                    if (coordinate.equals(prevSelectedBtnCoordinate)) {
                        shipButtonsIds.add(clickedButtonId);
                    } else {
                        if (prevSelectedBtnCoordinate.getY() == coordinate.getY()) {
                            // Direction is horizontal
                            int startX = prevSelectedBtnCoordinate.getX() < coordinate.getX()
                                    ? prevSelectedBtnCoordinate.getX() : coordinate.getX();

                            int endX = prevSelectedBtnCoordinate.getX() < coordinate.getX()
                                    ? coordinate.getX() : prevSelectedBtnCoordinate.getX();

                            for (int x = startX; x <= endX; x++) {
                                shipButtonsIds.add(buildButtonId(x, prevSelectedBtnCoordinate.getY()));
                            }
                        } else {
                            // Direction is vertical
                            int startY = prevSelectedBtnCoordinate.getY() < coordinate.getY()
                                    ? prevSelectedBtnCoordinate.getY() : coordinate.getY();

                            int endY = prevSelectedBtnCoordinate.getY() < coordinate.getY()
                                    ? coordinate.getY() : prevSelectedBtnCoordinate.getY();

                            for (int y = startY; y <= endY; y++) {
                                shipButtonsIds.add(buildButtonId(prevSelectedBtnCoordinate.getX(), y));
                            }
                        }
                    }

                    numOfShipsPlaced++;

                    isSelectingShip = false;

                    updateUI();
                }

                this.numShipPlacedSubject.onNext(numOfShipsPlaced);
                this.isSelectingShipSubject.onNext(this.isSelectingShip);
            }
        }
    }

    private void disableAllButtons() {
        buttons.forEach((buttonId, button) -> {
            button.setDisable(true);
        });
    }

    private void enableButtonsOnAxisOfCoordinate(Coordinate coordinate) {
        for (int x = coordinate.getX(); x < gridSize; x++) {
            final String buttonId = buildButtonId(x, coordinate.getY());

            if (shipButtonsIds.contains(buttonId)) {
                break;
            }

            buttons.get(buttonId).setDisable(false);
        }

        for (int x = coordinate.getX(); x >= 0; x--) {
            final String buttonId = buildButtonId(x, coordinate.getY());

            if (shipButtonsIds.contains(buttonId)) {
                break;
            }

            buttons.get(buttonId).setDisable(false);
        }

        for (int y = coordinate.getY(); y < gridSize; y++) {
            final String buttonId = buildButtonId(coordinate.getX(), y);

            if (shipButtonsIds.contains(buttonId)) {
                break;
            }

            buttons.get(buttonId).setDisable(false);
        }

        for (int y = coordinate.getY(); y >= 0; y--) {
            final String buttonId = buildButtonId(coordinate.getX(), y);

            if (shipButtonsIds.contains(buttonId)) {
                break;
            }

            buttons.get(buttonId).setDisable(false);
        }

    }

    private String buildButtonId(int x, int y) {
        return GRID_BUTTON + x + " " + y;
    }

    public void cancelShipSelection() {
        this.isSelectingShip = false;
        this.isSelectingShipSubject.onNext(this.isSelectingShip);
        updateUI();
    }
}
