package com.soen6441.battleship.view.console;

import com.soen6441.battleship.data.model.Ship;
import com.soen6441.battleship.enums.ShipDirection;
import com.soen6441.battleship.utils.GridUtils;
import com.soen6441.battleship.view.IView;
import com.soen6441.battleship.viewmodels.gameviewmodel.IGameViewModel;
import com.soen6441.battleship.viewmodels.shipplacementviewmodel.IShipPlacementViewModel;

import java.util.Scanner;
import java.util.logging.Logger;

public class ConsoleView implements IView {
    private static final Logger logger = Logger.getLogger(ConsoleView.class.getName());
    private IGameViewModel gameViewModel;
    private IShipPlacementViewModel shipPlacementViewModel;
    private Scanner scanner;

    public ConsoleView(IGameViewModel gameViewModel, IShipPlacementViewModel shipPlacementViewModel) {
        this.gameViewModel = gameViewModel;
        this.shipPlacementViewModel = shipPlacementViewModel;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void start() {
        getShipPlacementFromPlayer();

        gameViewModel.getPlayerGrid().subscribe(grid -> {
            logger.info("Player Grid:");
            GridUtils.printGrid(grid);
        });

        gameViewModel.getEnemyGrid().subscribe(grid -> {
            logger.info("Enemy Grid:");
            GridUtils.printGrid(grid);
        });

        gameViewModel.playerTurnChange().subscribe(name -> {
            logger.info("Player turn changed to: " + name.toUpperCase());
        });

        beginGame();
    }

    private void getShipPlacementFromPlayer() {
        while (true) {
            logger.info("Select an options:");
            logger.info("1. Add ship.");
            logger.info("2. Complete.");

            String input = scanner.nextLine();

            if (input.equals("1")) {
                try {
                    logger.info("Enter start coordinates (x y):");
                    input = scanner.nextLine();
                    String[] startCoordinates = input.split(" ");
                    int shipStartX = Integer.parseInt(startCoordinates[0]);
                    int shipStartY = Integer.parseInt(startCoordinates[1]);

                    logger.info("Enter end coordinates (x y):");
                    input = scanner.nextLine();
                    String[] endCoordinates = input.split(" ");
                    int shipEndX = Integer.parseInt(endCoordinates[0]);
                    int shipEndY = Integer.parseInt(endCoordinates[1]);

                    logger.info("Enter ship direction (h or v):");
                    input = scanner.nextLine();
                    ShipDirection shipDirection;
                    if (input.equals("h")) {
                        shipDirection = ShipDirection.HORIZONTAL;
                    } else if (input.equals("v")) {
                        shipDirection = ShipDirection.VERTICAL;
                    } else {
                        throw new Exception("Invalid Direction");
                    }

                    int shipLength;

                    if (shipDirection == ShipDirection.HORIZONTAL) {
                        shipLength = shipEndX - shipStartX + 1;
                    } else {
                        shipLength = shipEndY - shipStartY + 1;
                    }

                    Ship ship = new Ship.Builder()
                            .setStartCoordinates(shipStartX, shipStartY)
                            .setEndCoordinates(shipEndX, shipEndY)
                            .setLength(shipLength)
                            .setDirection(shipDirection)
                            .setName("New Ship")
                            .setUniqueId("OK")
                            .build();

                    shipPlacementViewModel.placeShip(ship);

                } catch (Exception e) {
                    logger.warning("Invalid input!");
                    logger.warning(e.getMessage());
                }
            } else if (input.equals("2")) {
                break;
            } else {
                logger.info("Please select a valid option!");
            }
        }
    }

    private void beginGame() {
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
