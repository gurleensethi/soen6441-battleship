package com.soen6441.battleship.view.gui.scenes.shipplacement;

import com.soen6441.battleship.view.gui.scenes.IScene;
import com.soen6441.battleship.viewmodels.shipplacementviewmodel.IShipPlacementViewModel;
import io.reactivex.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShipPlacementScene implements IScene {

    private final IShipPlacementViewModel shipPlacementViewModel;

    public ShipPlacementScene(IShipPlacementViewModel shipPlacementViewModel) {
        checkNotNull(shipPlacementViewModel);
        this.shipPlacementViewModel = shipPlacementViewModel;
    }

    @Override
    public Scene buildScene() {
        ShipPlacementGrid shipPlacementGrid = new ShipPlacementGrid(8);

        shipPlacementGrid.getShipAddedObservable().subscribe(shipPlacementViewModel::placeShip);

        // TODO: Move this to a separate Node class, maybe even fxml file.
        Node toolbar = buildToolbar(
                event -> shipPlacementGrid.cancelShipSelection(),
                null,
                shipPlacementGrid.getShipSelectionObservable(),
                shipPlacementGrid.getSelectedShipCountObservable()
        );

        // TODO: Move this to a separate Node class, maybe even fxml file.
        Node infoBar = buildInfoBar(
                shipPlacementGrid.getSelectedShipCountObservable()
        );

        VBox vBox = new VBox();
        vBox.getChildren().addAll(infoBar, toolbar, shipPlacementGrid);

        return new Scene(vBox);
    }

    private Node buildToolbar(
            EventHandler<ActionEvent> onCancelSelectionHandler,
            EventHandler<ActionEvent> onDoneHandler,
            Observable<Boolean> selectionObservable,
            Observable<Integer> shipPlacedCountObservable
    ) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 10, 10, 10));

        // Button to cancel current ship selection
        Button cancelSelectionButton = new Button();
        cancelSelectionButton.setDisable(true);
        cancelSelectionButton.setText("Cancel Selection");
        cancelSelectionButton.setOnAction(onCancelSelectionHandler);
        selectionObservable.subscribe(isSelecting -> cancelSelectionButton.setDisable(!isSelecting));

        Button doneButton = new Button();
        doneButton.setDisable(true);
        doneButton.setText("Done");
        doneButton.setOnAction(onDoneHandler);
        shipPlacedCountObservable.subscribe(count -> {
            doneButton.setDisable(count == 0);
        });

        Pane spacing = new Pane();
        HBox.setHgrow(spacing, Priority.ALWAYS);

        hBox.getChildren().addAll(spacing, cancelSelectionButton, doneButton);

        return hBox;
    }

    private Node buildInfoBar(
            Observable<Integer> shipPlacedCountObservable
    ) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 10, 10, 10));

        Text placedShipCountText = new Text("Ships Placed: 0/5");

        shipPlacedCountObservable.subscribe(count -> placedShipCountText.setText("Ships Placed: " + count + "/5"));

        hBox.getChildren().addAll(placedShipCountText);

        return hBox;
    }
}
