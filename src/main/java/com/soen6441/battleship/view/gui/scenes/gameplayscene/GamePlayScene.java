package com.soen6441.battleship.view.gui.scenes.gameplayscene;

import com.soen6441.battleship.view.gui.scenes.IScene;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GamePlayScene implements IScene {
    @Override
    public Scene buildScene() {
        VBox vBox = new VBox();
        vBox.setSpacing(40);
        vBox.setPadding(new Insets(20, 20, 20, 20));

        VBox enemyBoardVBox = new VBox();
        Text enemyTitleText = new Text("Enemy Board");
        enemyTitleText.setFont(new Font(30));
        GameGridPane enemyGameGrid = new GameGridPane(8, false);
        enemyBoardVBox.getChildren().addAll(enemyTitleText, enemyGameGrid);

        VBox playerBoardVBox = new VBox();
        Text playerTitleText = new Text("Player Board");
        playerTitleText.setFont(new Font(30));
        GameGridPane playerGameGrid = new GameGridPane(8, true);
        playerBoardVBox.getChildren().addAll(playerTitleText, playerGameGrid);

        vBox.getChildren().addAll(
                enemyBoardVBox,
                playerBoardVBox
        );

        return new Scene(vBox);
    }
}
