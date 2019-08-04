package com.soen6441.battleship.view.gui.scenes.gameplayscene;

import com.soen6441.battleship.services.gameconfig.GameConfig;
import com.soen6441.battleship.utils.TimerUtil;
import com.soen6441.battleship.view.gui.scenes.IScene;
import com.soen6441.battleship.viewmodels.gameviewmodel.IGameViewModel;
import com.sun.javafx.scene.layout.region.Margins;
import io.reactivex.schedulers.Schedulers;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.concurrent.ExecutorService;

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
//        Node enemyBoard = buildEnemyBoard();
//        Node playerBoard = buildPlayerBoard();

        // Set up left game box
        VBox gameBox = new VBox();
        gameBox.setSpacing(40);
        gameBox.setPadding(new Insets(20, 20, 20, 20));
        gameBox.getChildren().addAll(new GameGridPane3D());

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
                alert.setContentText(winnerText + "\nScore: " + gameViewModel.getFinalScore());
                alert.show();
            }
        });

        HBox root = new HBox();
        root.getChildren().addAll(gameBox, sideBar);

        gameViewModel.startGame();

        return new Scene(root);
    }

    /**
     * This method build enemy grid board, builds the grid pane on Stage with the given coordinate size and hides the details
     * from player.
     *
     * @return enemy grid board
     */
    private Node buildEnemyBoard() {
        // Set up enemy board
        VBox enemyBoardVBox = new VBox();
        Text enemyTitleText = new Text("Enemy Board");
        enemyTitleText.setFont(new Font(30));

        int gridSize = GameConfig.getsInstance().getGridSize();
        GameGridPane enemyGameGrid = new GameGridPane(gridSize, true);
        enemyGameGrid.setOnCoordinateHit(coordinate -> gameViewModel.sendHit(coordinate.getX(), coordinate.getY()));
        enemyBoardVBox.getChildren().addAll(enemyTitleText, enemyGameGrid);

        gameViewModel.getEnemyGrid().subscribe(enemyGameGrid::updateGrid);

        return enemyBoardVBox;
    }

    /**
     * This method build player grid board, builds the grid pane on Stage with the given coordinate size and shows the details
     * to player.
     *
     * @return player grid board
     */
    private Node buildPlayerBoard() {
        // Set up player board
        VBox playerBoardVBox = new VBox();
        Text playerTitleText = new Text("Player Board");
        playerTitleText.setFont(new Font(30));

        int gridSize = GameConfig.getsInstance().getGridSize();
        GameGridPane playerGameGrid = new GameGridPane(gridSize, false);
        gameViewModel.getPlayerGrid().subscribe(playerGameGrid::updateGrid);

        playerBoardVBox.getChildren().addAll(playerTitleText, playerGameGrid);

        return playerBoardVBox;
    }

    private Node buildSideBar() {
        VBox root = new VBox();
        root.setPadding(new Insets(16, 16, 16, 16));

        Text turnTimerText = new Text();
        turnTimerText.setFont(new Font(24));

        gameViewModel.turnTimer().subscribe(time -> {
            turnTimerText.setText("Turn Timer: \n" + TimerUtil.printableTime(time));
        });

        Text gameTimerText = new Text();
        gameTimerText.setFont(new Font(24));

        gameViewModel.gameTimer().subscribe(time -> {
            gameTimerText.setText("Game Timer: \n" + TimerUtil.printableTime(time));
        });

        root.getChildren().addAll(
                gameTimerText,
                turnTimerText
        );

        String gameModeText;

        if (GameConfig.getsInstance().isSalvaVariation()) {
            gameModeText = "Playing in Salva variation.";
        } else {
            gameModeText = "Playing in normal mode.";
        }

        final Text salvaVariationText = new Text(gameModeText);
        salvaVariationText.setFont(new Font(16));

        root.getChildren().addAll(salvaVariationText);

        return root;
    }
}
