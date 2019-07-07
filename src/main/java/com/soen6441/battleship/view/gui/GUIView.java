package com.soen6441.battleship.view.gui;

import com.soen6441.battleship.view.IView;
import com.soen6441.battleship.view.gui.navigator.SceneNavigator;
import com.soen6441.battleship.view.gui.scenes.shipplacement.ShipPlacementScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUIView extends Application implements IView {
    @Override
    public void start() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneNavigator.init(primaryStage);

        ShipPlacementScene scene = new ShipPlacementScene();
        primaryStage.setScene(scene.buildScene());
        primaryStage.show();
    }
}
