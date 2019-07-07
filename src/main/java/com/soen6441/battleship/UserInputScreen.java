package com.soen6441.battleship;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        gridSize = Integer.parseInt(gridSizeField.getText());
        try{
           loadScreen(event);
       }
       catch (Exception e){
            logger.info(e.getMessage());
       }
    }

    private void loadScreen(ActionEvent event) throws Exception{
        File file = new File("src/main/resources/GameWindow.fxml");
        Parent root = FXMLLoader.load(file.toURI().toURL());
        Scene userInputScreen =  new Scene(root,700, 400);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(userInputScreen);
        window.show();

    }


}
