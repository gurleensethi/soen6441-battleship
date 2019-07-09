package com.soen6441.battleship.view.gui;

import com.soen6441.battleship.common.SceneRoutes;
import com.soen6441.battleship.services.gamecontroller.GameController;
import com.soen6441.battleship.view.IView;
import com.soen6441.battleship.view.gui.navigator.SceneNavigator;
import com.soen6441.battleship.view.gui.scenes.gameplayscene.GamePlayScene;
import com.soen6441.battleship.view.gui.scenes.initialuserinputscene.InitialUserInputScene;
import com.soen6441.battleship.view.gui.scenes.shipplacement.ShipPlacementScene;
import com.soen6441.battleship.viewmodels.gameviewmodel.GameViewModel;
import com.soen6441.battleship.viewmodels.shipplacementviewmodel.IShipPlacementViewModel;
import com.soen6441.battleship.viewmodels.shipplacementviewmodel.ShipPlacementViewModel;
import javafx.application.Application;
import javafx.stage.Stage;

import static com.google.common.base.Preconditions.checkNotNull;

public class GUIView extends Application implements IView {

    private IShipPlacementViewModel shipPlacementViewModel;

    public GUIView() {

    }

    @Override
    public void start() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneNavigator.init(primaryStage);
        // TODO: Initialise routes somewhere else
        SceneNavigator.getInstance().registerRoute(SceneRoutes.INITIAL_USER_INPUT, InitialUserInputScene::new);
        SceneNavigator.getInstance().registerRoute(SceneRoutes.SHIP_PLACEMENT,
                () -> new ShipPlacementScene(new ShipPlacementViewModel(GameController.getInstance())));
        SceneNavigator.getInstance().registerRoute(SceneRoutes.GAME_PLAY, () -> {
            return new GamePlayScene(new GameViewModel(GameController.getInstance()));
        });

        // TODO: Get ShipPlacementViewModel from somewhere else, ideally use DI to inject it.
        primaryStage.show();

        SceneNavigator.getInstance().navigate(SceneRoutes.SHIP_PLACEMENT);
    }
}
