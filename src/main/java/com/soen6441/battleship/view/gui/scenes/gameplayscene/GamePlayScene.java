package com.soen6441.battleship.view.gui.scenes.gameplayscene;

import com.soen6441.battleship.view.gui.scenes.IScene;
import com.soen6441.battleship.viewmodels.gameviewmodel.IGameViewModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This class Game Play scene is the screen which updates the GUI after each move when players
 * makes moves. It displays the two boards and refresh the cells/buttons on every event.
 * The type Game play scene.
 */
public class GamePlayScene implements IScene {
    private IGameViewModel gameViewModel;

    /**
     * Instantiates a new Game play scene.
     *
     * @param gameViewModel the game view model
     */
    public GamePlayScene(IGameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
    }

    @Override
    public Scene buildScene() {
        Node enemyBoard = buildEnemyBoard();
        Node playerBoard = buildPlayerBoard();

        // Set up left game box
        VBox gameBox = new VBox();
        gameBox.setSpacing(40);
        gameBox.setPadding(new Insets(20, 20, 20, 20));
        gameBox.getChildren().addAll(enemyBoard, playerBoard);

        // Set up sidebar
        Node sideBar = buildSideBar();

        gameViewModel.isGameOver().subscribe(gameOverInfo -> {
            if (gameOverInfo.isGameOver()) {
                String winnerText = "";

                if (gameOverInfo.didPlayerWin()) {
                    winnerText = "YOU WON :D";
                } else {
                    winnerText = "YOU LOST :(";
                }

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Game Over!");
                alert.setContentText(winnerText);
                alert.show();
            }
        });

        HBox root = new HBox();
        root.getChildren().addAll(gameBox, sideBar);

        return new Scene(root);
    }

    /**
     * This method build enemy grid board, builds the grid pane on Stage with the given coordinate size and hides the details
     * from player.
     * @return enemy grid board
     */
    private Node buildEnemyBoard() {
        // Set up enemy board
        VBox enemyBoardVBox = new VBox();
        Text enemyTitleText = new Text("Enemy Board");
        enemyTitleText.setFont(new Font(30));
        //TODO : Make the grid size dynamic on user input
        GameGridPane enemyGameGrid = new GameGridPane(8, true);
        enemyGameGrid.setOnCoordinateHit(coordinate -> gameViewModel.sendHit(coordinate.getX(), coordinate.getY()));
        enemyBoardVBox.getChildren().addAll(enemyTitleText, enemyGameGrid);

        gameViewModel.getEnemyGrid().subscribe(enemyGameGrid::updateGrid);

        return enemyBoardVBox;
    }
    /**
     * This method build player grid board, builds the grid pane on Stage with the given coordinate size and shows the details
     * to player.
     * @return player grid board
     */
    private Node buildPlayerBoard() {
        // Set up player board
        VBox playerBoardVBox = new VBox();
        Text playerTitleText = new Text("Player Board");
        playerTitleText.setFont(new Font(30));
        //TODO : Make the grid size dynamic on user input
        GameGridPane playerGameGrid = new GameGridPane(8, false);
        gameViewModel.getPlayerGrid().subscribe(playerGameGrid::updateGrid);

        playerBoardVBox.getChildren().addAll(playerTitleText, playerGameGrid);

        return playerBoardVBox;
    }

    private Node buildSideBar() {
        VBox root = new VBox();

        Text text = new Text();

        gameViewModel.turnTimer().subscribe(time -> {
            text.setText("" + time);
        });

        root.getChildren().add(text);

        return root;
    }
}
