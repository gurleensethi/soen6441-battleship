package com.soen6441.battleship.view.gui.navigator;

import com.soen6441.battleship.exceptions.NavigatorNotInitialisedException;
import javafx.stage.Stage;

import static com.google.common.base.Preconditions.checkNotNull;

public class SceneNavigator {
    private static SceneNavigator sInstance;

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

    }
}
