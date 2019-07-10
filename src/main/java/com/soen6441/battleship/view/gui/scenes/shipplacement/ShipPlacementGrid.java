package com.soen6441.battleship.view.gui.scenes.shipplacement;

import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.*;
import java.util.logging.Logger;

class ShipPlacementGrid extends GridPane implements EventHandler<ActionEvent> {
    private static final Logger logger = Logger.getLogger(ShipPlacementGrid.class.getName());
    private static final String GRID_BUTTON = "GridButton:";
    private static final int MAX_SHIPS = 5;

    /**
     * Size of the ship currently allowed to be placed.
     */
    private int currentShipLength = 5;

    private int numOfShipsPlaced = 0;
    private final int gridSize;
    private Map<String, Button> buttons = new HashMap<>();
    private Map<String, Coordinate> buttonCoordinates = new HashMap<>();
    private Set<String> shipButtonsIds = new HashSet<>();
    private boolean isSelectingShip = false;
    private Coordinate prevSelectedBtnCoordinate;
    private PublishSubject<Boolean> isSelectingShipSubject = PublishSubject.create();
    private PublishSubject<Integer> numShipPlacedSubject = PublishSubject.create();
    private PublishSubject<Ship> shipAddedPublishSubject = PublishSubject.create();

    ShipPlacementGrid(int gridSize) {
        this.gridSize = gridSize;
        isSelectingShipSubject.onNext(this.isSelectingShip);
        numShipPlacedSubject.onNext(this.numOfShipsPlaced);
        initUI();
    }

    Observable<Boolean> getShipSelectionObservable() {
        return this.isSelectingShipSubject;
    }

    Observable<Integer> getSelectedShipCountObservable() {
        return this.numShipPlacedSubject;
    }

    Observable<Ship> getShipAddedObservable() {
        return this.shipAddedPublishSubject;
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
                button.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 0; -fx-border-radius: 0; -fx-border-color: darkgrey; -fx-border-width: 0.2;");
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

                    ShipDirection shipDirection;
                    int shipLength;
                    Coordinate shipStartCoordinates;
                    Coordinate shipEndCoordinates;

                    // Ship is only of size 1
                    if (coordinate.equals(prevSelectedBtnCoordinate)) {
                        shipButtonsIds.add(clickedButtonId);
                        shipDirection = ShipDirection.HORIZONTAL;
                        shipStartCoordinates = shipEndCoordinates = prevSelectedBtnCoordinate;
                        shipLength = 1;
                    } else {
                        if (prevSelectedBtnCoordinate.getY() == coordinate.getY()) {
                            // Direction is horizontal
                            shipDirection = ShipDirection.HORIZONTAL;

                            int startX = prevSelectedBtnCoordinate.getX() < coordinate.getX()
                                    ? prevSelectedBtnCoordinate.getX() : coordinate.getX();

                            int endX = prevSelectedBtnCoordinate.getX() < coordinate.getX()
                                    ? coordinate.getX() : prevSelectedBtnCoordinate.getX();

                            shipLength = endX - startX + 1;

                            shipStartCoordinates = new Coordinate(startX, coordinate.getY());
                            shipEndCoordinates = new Coordinate(endX, coordinate.getY());

                            for (int x = startX; x <= endX; x++) {
                                shipButtonsIds.add(buildButtonId(x, prevSelectedBtnCoordinate.getY()));
                            }
                        } else {
                            // Direction is vertical
                            shipDirection = ShipDirection.VERTICAL;

                            int startY = prevSelectedBtnCoordinate.getY() < coordinate.getY()
                                    ? prevSelectedBtnCoordinate.getY() : coordinate.getY();

                            int endY = prevSelectedBtnCoordinate.getY() < coordinate.getY()
                                    ? coordinate.getY() : prevSelectedBtnCoordinate.getY();

                            shipLength = endY - startY + 1;

                            shipStartCoordinates = new Coordinate(coordinate.getX(), startY);
                            shipEndCoordinates = new Coordinate(coordinate.getX(), endY);

                            for (int y = startY; y <= endY; y++) {
                                shipButtonsIds.add(buildButtonId(prevSelectedBtnCoordinate.getX(), y));
                            }
                        }
                    }

                    // Create new ship and add
                    Ship ship = new Ship.Builder()
                            .setUniqueId(UUID.randomUUID().toString())
                            .setName("Ship:" + numOfShipsPlaced)
                            .setDirection(shipDirection)
                            .setStartCoordinates(shipStartCoordinates.getX(), shipStartCoordinates.getY())
                            .setEndCoordinates(shipEndCoordinates.getX(), shipEndCoordinates.getY())
                            .setLength(shipLength)
                            .build();

                    this.shipAddedPublishSubject.onNext(ship);

                    numOfShipsPlaced++;
                    currentShipLength--;

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

    private boolean isCellValid(Coordinate coordinate) {
        return coordinate.getX() >= 0
                && coordinate.getY() >= 0
                && coordinate.getX() < gridSize
                && coordinate.getY() < gridSize;
    }

    private void enableButtonsOnAxisOfCoordinate(Coordinate coordinate) {
        // Check if top coordinate can be enabled
        Coordinate topCoordinates = new Coordinate(coordinate.getX(), coordinate.getY() - currentShipLength + 1);

        if (isCellValid(topCoordinates)) {
            boolean shouldPlace = true;

            for (int y = prevSelectedBtnCoordinate.getY(); y >= topCoordinates.getY(); y--) {
                if (shipButtonsIds.contains(buildButtonId(topCoordinates.getX(), y))) {
                    shouldPlace = false;
                }
            }

            if (shouldPlace) {
                buttons.get(buildButtonId(topCoordinates.getX(), topCoordinates.getY())).setDisable(false);
            }
        }

        // Check if down coordinates can be enabled
        Coordinate downCoordinates = new Coordinate(coordinate.getX(), coordinate.getY() + currentShipLength - 1);

        if (isCellValid(downCoordinates)) {
            boolean shouldPlace = true;

            for (int y = prevSelectedBtnCoordinate.getY(); y <= downCoordinates.getY(); y++) {
                if (shipButtonsIds.contains(buildButtonId(downCoordinates.getX(), y))) {
                    shouldPlace = false;
                }
            }

            if (shouldPlace) {
                buttons.get(buildButtonId(downCoordinates.getX(), downCoordinates.getY())).setDisable(false);
            }
        }

        // Check if left coordinates can be enabled
        Coordinate leftCoordinates = new Coordinate(coordinate.getX() - currentShipLength + 1, coordinate.getY());

        if (isCellValid(leftCoordinates) && !shipButtonsIds.contains(buildButtonId(leftCoordinates))) {
            boolean shouldPlace = true;

            for (int x = prevSelectedBtnCoordinate.getX(); x >= leftCoordinates.getX(); x--) {
                if (shipButtonsIds.contains(buildButtonId(x, leftCoordinates.getY()))) {
                    shouldPlace = false;
                }
            }

            if (shouldPlace) {
                buttons.get(buildButtonId(leftCoordinates.getX(), leftCoordinates.getY())).setDisable(false);
            }
        }

        // Check if up coordinates can be enabled
        Coordinate rightCoordinates = new Coordinate(coordinate.getX() + currentShipLength - 1, coordinate.getY());

        if (isCellValid(rightCoordinates) && !shipButtonsIds.contains(buildButtonId(rightCoordinates))) {
            boolean shouldPlace = true;

            for (int x = prevSelectedBtnCoordinate.getX(); x <= rightCoordinates.getX(); x++) {
                if (shipButtonsIds.contains(buildButtonId(x, rightCoordinates.getY()))) {
                    shouldPlace = false;
                }
            }

            if (shouldPlace) {
                buttons.get(buildButtonId(rightCoordinates.getX(), rightCoordinates.getY())).setDisable(false);
            }
        }

//        for (int x = coordinate.getX(); x < gridSize; x++) {
//            final String buttonId = buildButtonId(x, coordinate.getY());
//
//            if (shipButtonsIds.contains(buttonId)) {
//                break;
//            }
//
//            buttons.get(buttonId).setDisable(false);
//        }
//
//        for (int x = coordinate.getX(); x >= 0; x--) {
//            final String buttonId = buildButtonId(x, coordinate.getY());
//
//            if (shipButtonsIds.contains(buttonId)) {
//                break;
//            }
//
//            buttons.get(buttonId).setDisable(false);
//        }
//
//        for (int y = coordinate.getY(); y < gridSize; y++) {
//            final String buttonId = buildButtonId(coordinate.getX(), y);
//
//            if (shipButtonsIds.contains(buttonId)) {
//                break;
//            }
//
//            buttons.get(buttonId).setDisable(false);
//        }
//
//        for (int y = coordinate.getY(); y >= 0; y--) {
//            final String buttonId = buildButtonId(coordinate.getX(), y);
//
//            if (shipButtonsIds.contains(buttonId)) {
//                break;
//            }
//
//            buttons.get(buttonId).setDisable(false);
//        }

    }

    private String buildButtonId(int x, int y) {
        return GRID_BUTTON + x + " " + y;
    }

    private String buildButtonId(Coordinate coordinate) {
        return GRID_BUTTON + coordinate.getX() + " " + coordinate.getY();
    }

    public void cancelShipSelection() {
        this.isSelectingShip = false;
        this.isSelectingShipSubject.onNext(this.isSelectingShip);
        updateUI();
    }
}
