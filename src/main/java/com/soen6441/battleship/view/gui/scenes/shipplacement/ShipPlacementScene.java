package com.soen6441.battleship.view.gui.scenes.shipplacement;

import com.soen6441.battleship.common.SceneRoutes;
import com.soen6441.battleship.view.gui.navigator.SceneNavigator;
import com.soen6441.battleship.view.gui.scenes.IScene;
import com.soen6441.battleship.viewmodels.shipplacementviewmodel.IShipPlacementViewModel;
import io.reactivex.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
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

        Node shipBar = buildVerticalShipButtons();
        Node shipBar2 = buildHorizontalShipButtons();


        VBox vBox = new VBox();
        vBox.getChildren().addAll(infoBar, toolbar, shipPlacementGrid);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox,shipBar,shipBar2);

        return new Scene(hBox);
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

        hBox.getChildren().addAll(spacing,cancelSelectionButton, doneButton);

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

    private Node buildVerticalShipButtons(){

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(100, 10, 10, 10));

        Text verticalShipLabel = new Text("Vertical");

        Button ship1v = createShipButtons("https://static.thenounproject.com/png/12287-200.png",6);

        Button ship2v = createShipButtons("https://static.thenounproject.com/png/12287-200.png",7);

        Button ship3v = createShipButtons("https://static.thenounproject.com/png/12287-200.png",8);

        Button ship4v = createShipButtons("https://static.thenounproject.com/png/12287-200.png",9);

        Button ship5v = createShipButtons("https://static.thenounproject.com/png/12287-200.png",10);

        vBox.getChildren().addAll(verticalShipLabel,ship1v,ship2v,ship3v,ship4v,ship5v);


        return vBox;

    }
    private Node buildHorizontalShipButtons(){

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(100, 10, 10, 10));
        Text horizontalShipLabel = new Text("Horizontal");

        Button ship1h = createShipButtons("https://static.thenounproject.com/png/12287-200.png",1);

        Button ship2h = createShipButtons("https://static.thenounproject.com/png/12287-200.png",2);

        Button ship3h = createShipButtons("https://static.thenounproject.com/png/12287-200.png",3);

        Button ship4h = createShipButtons("https://static.thenounproject.com/png/12287-200.png",4);

        Button ship5h = createShipButtons("https://static.thenounproject.com/png/12287-200.png",5);

        vBox.getChildren().addAll(horizontalShipLabel,ship1h,ship2h,ship3h,ship4h,ship5h);

        return vBox;

    }

    private Button createShipButtons(String imageURL,int shipSize){


        //TODO: Utilize shipSize

        Button shipButton = new Button();
        Image img = new Image(imageURL);

        ImageView view = new ImageView(img);
        view.setFitHeight(30);
        view.setFitWidth(40);


        shipButton.setGraphic(view);

        //Drag detected event handler is used for adding drag functionality to the boat node
        shipButton.setOnDragDetected(event -> {
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

        return shipButton;

    }




}
