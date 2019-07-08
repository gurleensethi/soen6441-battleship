package com.soen6441.battleship.view.gui.scenes.gameplayscene;

import com.soen6441.battleship.view.gui.scenes.IScene;
import com.soen6441.battleship.viewmodels.gameviewmodel.IGameViewModel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GamePlayScene implements IScene {
    private IGameViewModel gameViewModel;

    public GamePlayScene(IGameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
    }

    @Override
    public Scene buildScene() {
        VBox vBox = new VBox();
        vBox.setSpacing(40);
        vBox.setPadding(new Insets(20, 20, 20, 20));

        // Set up enemy board
        VBox enemyBoardVBox = new VBox();
        Text enemyTitleText = new Text("Enemy Board");
        enemyTitleText.setFont(new Font(30));
        GameGridPane enemyGameGrid = new GameGridPane(8, false);
        enemyBoardVBox.getChildren().addAll(enemyTitleText, enemyGameGrid);

        gameViewModel.getEnemyGrid().subscribe(enemyGameGrid::updateGrid);

        // Set up player board
        VBox playerBoardVBox = new VBox();
        Text playerTitleText = new Text("Player Board");
        playerTitleText.setFont(new Font(30));

        GameGridPane playerGameGrid = new GameGridPane(8, true);
        playerGameGrid.setOnCoordinateHit(coordinate -> gameViewModel.sendHit(coordinate.getX(), coordinate.getY()));
        gameViewModel.getPlayerGrid().subscribe(playerGameGrid::updateGrid);


        gameViewModel.playerTurnChange().subscribe(player -> {
            enemyGameGrid.setOverlayEnabled(player.equals("player"));
            playerGameGrid.setOverlayEnabled(player.equals("enemy"));
        });

        playerBoardVBox.getChildren().addAll(playerTitleText, playerGameGrid);

        vBox.getChildren().addAll(
                enemyBoardVBox,
                playerBoardVBox
        );


        return new Scene(vBox);
    }
}
