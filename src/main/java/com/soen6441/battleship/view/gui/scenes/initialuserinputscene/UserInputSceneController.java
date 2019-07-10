package com.soen6441.battleship.view.gui.scenes.initialuserinputscene;

import com.soen6441.battleship.common.SceneRoutes;
import com.soen6441.battleship.view.gui.navigator.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * The type User input scene controller is the controller for Welcome screen fxml,
 * takes grid size and name from the user and passes data to view model and ship placement.
 */
public class UserInputSceneController {
    final private Logger logger = Logger.getLogger(UserInputSceneController.class.getName());
    private int gridSize;
    private String playerName;
    @FXML
    private TextField gridSizeField;
    @FXML
    private TextField nameField;

    /**
     * Start action:  This method start action is called on all the events from GUI
     * displays the alert messages on the GUI and validates the input from the user.
     *
     * @param event the event
     */
    @FXML
    protected void startAction(ActionEvent event) {

        //Alert message for the invalid inputs
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
            //Navigate to ship placement screen
            SceneNavigator.getInstance().navigate(SceneRoutes.SHIP_PLACEMENT);
        }
    }
}
