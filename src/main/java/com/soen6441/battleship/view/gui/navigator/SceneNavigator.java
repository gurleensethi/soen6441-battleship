package com.soen6441.battleship.view.gui.navigator;

import com.soen6441.battleship.exceptions.InvalidRouteException;
import com.soen6441.battleship.exceptions.NavigatorNotInitialisedException;
import com.soen6441.battleship.view.gui.scenes.IScene;
import com.soen6441.battleship.view.gui.scenes.ISceneBuilder;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class SceneNavigator {
    private static final Logger logger = Logger.getLogger(SceneNavigator.class.getName());
    private static SceneNavigator sInstance;
    private Stage primaryStage;
    private Map<String, ISceneBuilder> routes = new HashMap<>();

    public static void init(Stage primaryStage) {
        checkNotNull(primaryStage);
        sInstance = new SceneNavigator(primaryStage);
    }

    public static SceneNavigator getInstance() {
        if (sInstance == null) {
            throw new NavigatorNotInitialisedException();
        }
        return sInstance;
    }

    private SceneNavigator(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void registerRoute(String routeName, ISceneBuilder sceneBuilder) {
        checkNotNull(routeName);
        checkNotNull(sceneBuilder);
        logger.info("Adding route to navigator: " + routeName);
        routes.put(routeName, sceneBuilder);
    }

    public void navigate(String route) {
        if (!routes.containsKey(route)) {
            throw new InvalidRouteException();
        }
        Scene scene = routes.get(route).buildScene().buildScene();
        primaryStage.setScene(scene);
    }
}
