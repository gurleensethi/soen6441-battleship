package com.soen6441.battleship.view.gui.scenes;

import javafx.scene.Scene;

@FunctionalInterface()
public interface ISceneBuilder {
    IScene buildScene();
}
