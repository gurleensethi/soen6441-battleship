package com.soen6441.battleship.view.gui.scenes.shipplacement;

import com.soen6441.battleship.view.IView;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.io.File;

public class ShipPlacementGrid3D extends Application implements IView {

    @Override
    public void start() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Create a box
        Box box = prepareBox();
        Group group = new Group();
        group.getChildren().add(box);

        //Create camera and scene
        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, 1000, 600);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        //Place the box object in the position required.
        box.setTranslateX(500);
        box.setTranslateY(300);
        box.setTranslateZ(-800);
        Transform transform = new Rotate(65, new Point3D(1,1,0));
        box.getTransforms().add(transform);

        //Create stage and show
        primaryStage.setTitle("This is a test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Box prepareBox() {

//        PhongMaterial material = new PhongMaterial();
//        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/src/main/resources/water.jpg")));
        Box box = new Box(100, 20, 50);
//        box.setMaterial(material);
        return box;
    }
}