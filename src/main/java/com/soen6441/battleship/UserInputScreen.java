package com.soen6441.battleship;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class UserInputScreen {
    Button button;
    Logger logger = Logger.getLogger(UserInputScreen.class.getName());
    private int gridSize;
    @FXML private TextField gridSizeField;

    @FXML
    protected void startAction(ActionEvent event) {

        if (gridSizeField.getText() == null || gridSizeField.getText().trim().isEmpty()
                || !gridSizeField.getText().matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid input type from user!");
            alert.setContentText("Please input in Numeric from 0-9!");
            alert.showAndWait();
            logger.config("Empty or invalid field");
        }else{
            gridSize = Integer.parseInt(gridSizeField.getText());
            logger.info("Value given from user:"+ gridSize);
        }
        try{
           loadScreen(event);
       }
       catch (Exception e){
            logger.info(e.getMessage());
       }
    }

    private void loadScreen(ActionEvent event) throws Exception{
        File file = new File("src/main/resources/WelcomeScreen.fxml");
        Parent root = FXMLLoader.load(file.toURI().toURL());
        Scene userInputScreen =  new Scene(root,700, 400);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(userInputScreen);
        window.show();

    }
}
