package com.soen6441.battleship.view.gui.scenes.shipplacement;

import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.services.gameconfig.GameConfig;
import com.soen6441.battleship.view.IView;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ShipPlacementGrid3D extends Application implements IView {

    private Map<String, Box> boxes = new HashMap<>();
    private Map<String, Coordinate> boxCoordinates = new HashMap<>();
    private static final String GRID_BUTTON = "GridButton:";

    @Override
    public void start() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Create a group
        Group group = new Group();

        int gridSize = GameConfig.getsInstance().getGridSize();

        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                Box box = prepareBox();
                String id = buildButtonId(x, y);
                boxes.put(id, box);

                //Place the box object in the position required.
                box.setTranslateX(500+10);
                box.setTranslateY(300+10);
//                box.setTranslateZ(-800-100);
                Transform transform = new Rotate(65, new Point3D(1,0,0));
                box.getTransforms().add(transform);

                Coordinate coordinate = new Coordinate(x, y);
                boxCoordinates.put(id, coordinate);

                // Add button to grid
                group.getChildren().add(box);
            }
        }

        //Create camera and scene
        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, 1000, 600);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        //Create stage and show
        primaryStage.setTitle("This is a test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private String buildButtonId(int x, int y) {
        return GRID_BUTTON + x + " " + y;
    }

    private Box prepareBox() {

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/src/main/resources/water.jpg")));
        Box box = new Box(10, 20, 50);
        box.setMaterial(material);
        return box;
    }

}