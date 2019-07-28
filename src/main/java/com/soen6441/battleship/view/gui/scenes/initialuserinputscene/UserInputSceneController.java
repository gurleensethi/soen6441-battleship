package com.soen6441.battleship.view.gui.scenes.initialuserinputscene;

import com.soen6441.battleship.common.SceneRoutes;
import com.soen6441.battleship.view.gui.navigator.SceneNavigator;
import com.soen6441.battleship.viewmodels.initiuserviewmodel.InitUserViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleGroup;

import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * The type User input scene controller is the controller for Welcome screen fxml,
 * takes grid size and name from the user and passes data to view model and ship placement.
 */
public class UserInputSceneController {
    final private Logger logger = Logger.getLogger(UserInputSceneController.class.getName());
    private InitUserViewModel initUserViewModel = new InitUserViewModel();

    private String playerName;
    @FXML
    private TextField nameField;
    @FXML
    private RadioButton salvaNoRadioButton;
    @FXML
    private RadioButton salvaYesRadioButton;

    @FXML
    void initialize() {
        salvaNoRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initUserViewModel.setSalve(false);
            }
        });

        salvaYesRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initUserViewModel.setSalve(true);
            }
        });
    }

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
        if (nameField.getText().trim().isEmpty()
                || nameField.getText() == null
                || !Pattern.matches(".*[a-zA-Z]+.*", nameField.getText())) {

            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid input type from user!");
            alert.setContentText("Player Name should be in alphabets from A-Z !");
            alert.showAndWait();
            logger.config("Empty or invalid name field.");
        } else {
            playerName = nameField.getText();
            initUserViewModel.setName(playerName);
            //Navigate to ship placement screen
            SceneNavigator.getInstance().navigate(SceneRoutes.SHIP_PLACEMENT);
        }
    }
}