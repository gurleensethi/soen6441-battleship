package com.soen6441.battleship.view.console;

import com.soen6441.battleship.utils.GridUtils;
import com.soen6441.battleship.view.IView;
import com.soen6441.battleship.viewmodels.gameviewmodel.IGameViewModel;

import java.util.Scanner;
import java.util.logging.Logger;

public class ConsoleView implements IView {
    private static final Logger logger = Logger.getLogger(ConsoleView.class.getName());
    private IGameViewModel gameViewModel;

    public ConsoleView(IGameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
    }

    @Override
    public void start() {
        gameViewModel.getPlayerGrid().subscribe(grid -> {
            logger.info("Player Grid:");
            GridUtils.printGrid(grid);
        });

        gameViewModel.getEnemyGrid().subscribe(grid -> {
            logger.info("Enemy Grid:");
            GridUtils.printGrid(grid);
        });

        beginGame();
    }

    private void beginGame() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("exit")) {
                break;
            }

            try {
                String[] coordinates = input.split(" ");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                gameViewModel.sendHit(x, y);
            } catch (Exception e) {
                logger.warning("Invalid Input!");
                logger.warning(e.getMessage());
            }
        }

        scanner.close();
    }
}
