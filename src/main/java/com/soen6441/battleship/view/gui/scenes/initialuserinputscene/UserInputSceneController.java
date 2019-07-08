package com.soen6441.battleship.view.gui.scenes.initialuserinputscene;

import com.soen6441.battleship.common.SceneRoutes;
import com.soen6441.battleship.view.gui.navigator.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class UserInputSceneController {
    final private Logger logger = Logger.getLogger(UserInputSceneController.class.getName());
    private int gridSize;
    private String playerName;
    @FXML
    private TextField gridSizeField;
    @FXML
    private TextField nameField;

    @FXML
    protected void startAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        if (gridSizeField.getText() == null
                || gridSizeField.getText().trim().isEmpty()
                || !gridSizeField.getText().matches("\\d+")
                || Integer.parseInt(gridSizeField.getText()) < 8) {

            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid input type from user!");
            alert.setContentText("Please Enter grid size value of greater than 8.\n" +
                    "(Note: Grid size value can only be of type Numeric!)");
            alert.showAndWait();
            logger.config("Empty or invalid grid size field");
            return;

        } else if (nameField.getText().trim().isEmpty()
                || nameField.getText() == null
                || !Pattern.matches(".*[a-zA-Z]+.*", nameField.getText())) {

            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid input type from user!");
            alert.setContentText("Player Name should be in alphabets from A-Z !");
            alert.showAndWait();
            logger.config("Empty or invalid name field.");
            return;

        } else {
            gridSize = Integer.parseInt(gridSizeField.getText());
            playerName = nameField.getText();
            logger.info("Values from input:" + " Player Name = " + playerName + " Grid Size : " + gridSize);
            SceneNavigator.getInstance().navigate(SceneRoutes.SHIP_PLACEMENT);
        }
    }
}
