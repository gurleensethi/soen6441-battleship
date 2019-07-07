package com.soen6441.battleship.view.gui.scenes.shipplacement;

import com.soen6441.battleship.view.gui.scenes.IScene;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ShipPlacementScene implements IScene {

    @Override
    public Scene buildScene() {
        ShipPlacementGrid shipPlacementGrid = new ShipPlacementGrid(8);
        VBox vBox = new VBox();
        vBox.getChildren().add(new Text("OK"));
        vBox.getChildren().add(shipPlacementGrid);
        return new Scene(vBox);
    }
}
