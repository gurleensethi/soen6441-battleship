package com.soen6441.battleship;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class WelcomeScreenController {
    Button button;
    Logger logger = Logger.getLogger(WelcomeScreenController.class.getName());

    @FXML
    protected void startAction(ActionEvent event) {
        logger.info("Start method was called");
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
        Scene gameWindowScene =  new Scene(root,700, 400);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gameWindowScene);
        window.show();

    }


}
