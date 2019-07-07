package com.soen6441.battleship.view.gui.scenes.initialuserinputscene;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class UserInputScreen {
    final private Logger logger = Logger.getLogger(UserInputScreen.class.getName());
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
        }
    }
}
