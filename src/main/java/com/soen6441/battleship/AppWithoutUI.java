package com.soen6441.battleship;

import com.soen6441.battleship.services.gamecontroller.GameController;
import com.soen6441.battleship.view.console.ConsoleView;
import com.soen6441.battleship.viewmodels.gameviewmodel.GameViewModel;
import com.soen6441.battleship.viewmodels.gameviewmodel.IGameViewModel;


public class AppWithoutUI {
    public static void main(String[] args) {
        GameController.getInstance().createOrGetPlayer("player");
        IGameViewModel gameViewModel = new GameViewModel(GameController.getInstance());
        ConsoleView consoleView = new ConsoleView(gameViewModel);
        consoleView.start();
    }
}
