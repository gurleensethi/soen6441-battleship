package com.soen6441.battleship.view.gui.scenes.shipplacement;

import com.soen6441.battleship.common.SceneRoutes;
import com.soen6441.battleship.view.gui.navigator.SceneNavigator;
import com.soen6441.battleship.view.gui.scenes.IScene;
import com.soen6441.battleship.viewmodels.shipplacementviewmodel.IShipPlacementViewModel;
import io.reactivex.Observable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The type Ship placement scene: This class has is the GUI of the Ship placement scene, it contains a grid
 * which has buttons of gridsize^2.
 */
public class ShipPlacementScene implements IScene {

    private final IShipPlacementViewModel shipPlacementViewModel;
    ShipPlacementGrid shipPlacementGrid = new ShipPlacementGrid(8);


    /**
     * Instantiates a new Ship placement scene.
     *
     * @param shipPlacementViewModel the ship placement view model
     */
    public ShipPlacementScene(IShipPlacementViewModel shipPlacementViewModel) {
        checkNotNull(shipPlacementViewModel);
        this.shipPlacementViewModel = shipPlacementViewModel;
    }

    /**
     * This method build scene is an overridden method to build scene for ship placement.
     *
     * @return scene
     */
    @Override
    public Scene buildScene() {

        shipPlacementGrid.getShipAddedObservable().subscribe(shipPlacementViewModel::placeShip);

        // TODO: Move this to a separate Node class, maybe even fxml file.
        Node toolbar = buildToolbar(
                event -> shipPlacementGrid.cancelShipSelection(),
                event -> SceneNavigator.getInstance().navigate(SceneRoutes.GAME_PLAY),
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

    /**
     * This method buildtoolbar is the GUI which gives two selections to user
     * Cancel selection and Done, once the 5 ships are placed the done button works as navigator to game play scene.
     *
     * @param onCancelSelectionHandler
     * @param onDoneHandler
     * @param selectionObservable
     * @param shipPlacedCountObservable
     * @return Node Hbox
     */
    private Node buildToolbar(
            EventHandler<ActionEvent> onCancelSelectionHandler,
            EventHandler<ActionEvent> onDoneHandler,
            Observable<Boolean> selectionObservable,
            Observable<Integer> shipPlacedCountObservable
    ) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 10, 10, 10));

        Image img = new Image("https://i.pinimg.com/564x/61/02/33/610233eb937b2119fde0dd7443d9114c.jpg");
        ImageView view = new ImageView(img);
        view.setFitHeight(10);
        view.setFitWidth(30);

        Button shipButton = new Button();
        shipButton.setGraphic(view);

        //Drag detected event handler is used for adding drag functionality to the boat node
        shipButton.setOnDragDetected(event -> {
            //Drag was detected, start drap-and-drop gesture
            //Allow any transfer node
            Dragboard db = shipButton.startDragAndDrop(TransferMode.MOVE);

            //Put ImageView on dragboard
            ClipboardContent cbContent = new ClipboardContent();
            cbContent.putImage(img);

            //cbContent.put(DataFormat.)
            db.setContent(cbContent);
            // shipButton.setVisible(false);
            event.consume();

        });


        shipButton.setOnDragDropped(event -> {

            System.out.println("----------------------dropped");

        });

        shipButton.setOnDragDone(event -> {
            //the drag and drop gesture has ended
            //if the data was successfully moved, clear it
            System.out.println("Transfer mode = " + event.getTransferMode());
            if(event.getTransferMode() == TransferMode.MOVE){
                shipButton.setVisible(false);
            }
            event.consume();

        });


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
            doneButton.setDisable(count != 5);
        });

        Pane spacing = new Pane();
        HBox.setHgrow(spacing, Priority.ALWAYS);

        hBox.getChildren().addAll(spacing, shipButton,cancelSelectionButton, doneButton);

        return hBox;
    }

    /**
     * This method buildInfoBar displays the status of ship placement.
     *
     * @param shipPlacedCountObservable
     * @return hBox
     */
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
