package com.soen6441.battleship.view.gui.scenes.shipplacement;

import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    private Map<String,
            Button> buttons = new HashMap<>();
    private Map<String,
            Coordinate> buttonCoordinates = new HashMap<>();
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
                button.setPrefWidth(50);
                button.setPrefHeight(50);
                button.setOnAction(this);
                button.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 0; -fx-border-radius: 0; -fx-border-color: darkgrey; -fx-border-width: 0.2;");
                buttons.put(id, button);


                // Drag over event handler is used for the receiving node to allow movement
                button.setOnDragOver(event -> {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                });

                //Drag entered changes the appearance of the receiving node to indicate to the player that they can place there
                button.setOnDragEntered(event -> {
                    //The drag-and-drop gesture entered the target
                    //show the user that it is an actual gesture target

                    String[] shipInfo = ((Button) event.getGestureSource()).getId().split(" ");
                    ShipDirection shipDirection = shipInfo[0].equals("v") ? ShipDirection.VERTICAL : ShipDirection.HORIZONTAL;
                    int shipLength = Integer.parseInt(shipInfo[1]);

                    if (event.getSource() instanceof Button) {
                        String buttonId = ((Button) event.getSource()).getId();

                        if (buttonId.startsWith(GRID_BUTTON)) {
                            Coordinate buttonCoordinate = buttonCoordinates.get(buttonId);
                            highlightHoveringButtons(shipDirection, shipLength, buttonCoordinate);
                        }
                    }

                    logger.info("Drag entered at " + button.getId());

                    event.consume();

                });

                // Drag dropped draws the image to the receiving node
                button.setOnDragDropped(event -> {
                    String[] shipInfo = ((Button) event.getGestureSource()).getId().split(" ");
                    ShipDirection shipDirection = shipInfo[0].equals("v") ? ShipDirection.VERTICAL : ShipDirection.HORIZONTAL;
                    int shipLength = Integer.parseInt(shipInfo[1]);

                    //If there is an image on the drag board, read it and use it
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    Node node = event.getPickResult().getIntersectedNode();
                    if (node != button && db.hasImage()) {

                        int xShipInitial = buttonCoordinates.get(button.getId()).getX();
                        int yShipInitial = buttonCoordinates.get(button.getId()).getY();

                        ImageView image = new ImageView(db.getImage());
                        image.setFitWidth(button.getWidth() / 2);
                        image.setFitHeight(button.getHeight() / 2);

                        // TODO: set image size; use correct column/row span
                        logger.info("Placing ship at " + xShipInitial + ", " + yShipInitial);

                        String buttonId = ((Button) event.getSource()).getId();
                        Coordinate buttonCoordinate = buttonCoordinates.get(buttonId);
                        if (shipDirection == ShipDirection.HORIZONTAL) {
                            if ((buttonCoordinate.getX() + shipLength - 1) < 8) {
                                logger.info(shipDirection.toString() + " " + shipLength + " " + buttonCoordinate.toString());
                                for (int i = buttonCoordinate.getX(); i < (buttonCoordinate.getX() + shipLength); i++) {
                                    String newButtonId = buildButtonId(new Coordinate(i, buttonCoordinate.getY()));
                                    buttons.get(newButtonId).setStyle("-fx-background-color: LIGHTBLUE; -fx-background-radius: 0; -fx-border-radius: 0; -fx-border-color: darkgrey; -fx-border-width: 0.2;");
                                    buttons.get(newButtonId).setText("");
                                }
                            }
                        } else {
                            if ((buttonCoordinate.getY() + shipLength - 1) < 8) {
                                for (int j = buttonCoordinate.getY(); j < (buttonCoordinate.getY() + shipLength); j++) {
                                    String newButtonId = buildButtonId(new Coordinate(buttonCoordinate.getX(), j));
                                    buttons.get(newButtonId).setStyle("-fx-background-color: LIGHTBLUE; -fx-background-radius: 0; -fx-border-radius: 0; -fx-border-color: darkgrey; -fx-border-width: 0.2;");
                                    buttons.get(newButtonId).setText("");
                                }
                            }
                        }
//                        button.setGraphic(image);
                        success = true;
                    }
                    //let the source know whether the image was successfully transferred and used
                    event.setDropCompleted(success);

                    event.consume();

                });
                //Drag exited reverts the appearance of the receiving node when the mouse is outside of the node
                button.setOnDragExited(event -> {
                    //mouse moved away, remove graphical cues
                    button.setOpacity(1);
                    event.consume();
                    resetHighlight();
                });

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

    private void resetHighlight() {
        for (Button button : buttons.values()) {
            button.setOpacity(1.0);
        }
    }

    private void highlightHoveringButtons(ShipDirection shipDirection, int shipLength, Coordinate coordinate) {
        if (shipDirection == ShipDirection.HORIZONTAL) {
            if ((coordinate.getX() + shipLength - 1) < 8) {
                logger.info(shipDirection.toString() + " " + shipLength + " " + coordinate.toString());
                for (int x = coordinate.getX(); x < (coordinate.getX() + shipLength); x++) {
                    String buttonId = buildButtonId(new Coordinate(x, coordinate.getY()));
                    buttons.get(buttonId).setOpacity(0.5);
                }
            }
        } else {
            if ((coordinate.getY() + shipLength - 1) < 8) {
                for (int y = coordinate.getY(); y < (coordinate.getY() + shipLength); y++) {
                    String buttonId = buildButtonId(new Coordinate(coordinate.getX(), y));
                    buttons.get(buttonId).setOpacity(0.5);
                }
            }
        }
    }

    private void placeShip(ShipDirection shipDirection, int shipLength, Coordinate coordinate) {

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

                            int startX = prevSelectedBtnCoordinate.getX() < coordinate.getX() ?
                                    prevSelectedBtnCoordinate.getX() : coordinate.getX();

                            int endX = prevSelectedBtnCoordinate.getX() < coordinate.getX() ?
                                    coordinate.getX() : prevSelectedBtnCoordinate.getX();

                            shipLength = endX - startX + 1;

                            shipStartCoordinates = new Coordinate(startX, coordinate.getY());
                            shipEndCoordinates = new Coordinate(endX, coordinate.getY());

                            for (int x = startX; x <= endX; x++) {
                                shipButtonsIds.add(buildButtonId(x, prevSelectedBtnCoordinate.getY()));
                            }
                        } else {
                            // Direction is vertical
                            shipDirection = ShipDirection.VERTICAL;

                            int startY = prevSelectedBtnCoordinate.getY() < coordinate.getY() ?
                                    prevSelectedBtnCoordinate.getY() : coordinate.getY();

                            int endY = prevSelectedBtnCoordinate.getY() < coordinate.getY() ?
                                    coordinate.getY() : prevSelectedBtnCoordinate.getY();

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
        return coordinate.getX() >= 0 &&
                coordinate.getY() >= 0 &&
                coordinate.getX() < gridSize &&
                coordinate.getY() < gridSize;
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