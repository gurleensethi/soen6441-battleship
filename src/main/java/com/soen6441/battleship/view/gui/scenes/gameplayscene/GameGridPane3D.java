package com.soen6441.battleship.view.gui.scenes.gameplayscene;

import com.soen6441.battleship.data.model.Coordinate;
import com.soen6441.battleship.services.gameconfig.GameConfig;
import com.soen6441.battleship.view.gui.scenes.IScene;
import com.soen6441.battleship.viewmodels.gameviewmodel.IGameViewModel;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.*;

public class GameGridPane3D extends HBox {
    private static final String GRID_BOX = "GridBox:";
    private static final int BOX_PADDING = 11;

    private final int gridSize;

    private final GridPane boxesGridPane = new GridPane();
    private final StackPane overlayPane = new StackPane();
    private IOnCoordinateHit onCoordinateHit;
    private Map<String, Button> boxes = new HashMap<>();
    private Map<String, Coordinate> boxCoordinates = new HashMap<>();
    private Set<String> shipBoxIds = new HashSet<>();

    public GameGridPane3D() {
        this.gridSize = GameConfig.getsInstance().getGridSize();
        initUI();
    }


    private void initUI() {
        Box testBox = new Box(5, 5, 5);

        // Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
                new Rotate(35, Rotate.X_AXIS),
                new Rotate(-25, Rotate.Y_AXIS),
                new Rotate(-20, Rotate.Z_AXIS),
                new Translate(-20, 120, -550)
        );
        camera.setNearClip(0.1);
        camera.setFarClip(10000);

        // Build the Scene Graph
        Group root = new Group();
        root.getChildren().add(camera);
//        root.getChildren().add(testBox);
//        testBox.setOnMouseClicked(event -> {
//            testBox.getTransforms().addAll(
//                    new Translate(10, 0,0)
//            );
//        });

        int gridSize = GameConfig.getsInstance().getGridSize();

        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                Box box = new Box(10, 10, 10);
                box.setMaterial(new PhongMaterial(Color.SKYBLUE));
                box.setOnMouseClicked(event -> {
                    System.out.println("OKOK");
                });
                box.setOnMouseEntered(event -> {
                    box.setMaterial(new PhongMaterial(Color.ORANGE));
                    root.getScene().setCursor(Cursor.HAND);
                    box.getTransforms().addAll(new Translate(0, 0, -0.5));
                });
                box.setOnMouseExited(event -> {
                    box.setMaterial(new PhongMaterial(Color.SKYBLUE));
                    root.getScene().setCursor(Cursor.DEFAULT);
                    box.getTransforms().addAll(new Translate(0, 0, 0.5));
                });
                root.getChildren().add(box);
                box.getTransforms().addAll(new Translate(x * 11, y * 11, 0));
                animateSphere(box, x * 11, y * 11, x * 200);
            }
        }

        for (int x = 0; x < gridSize; x++) {
            for (int y = BOX_PADDING; y < gridSize + BOX_PADDING; y++) {
                Box box = new Box(10, 10, 10);
                box.setOnMouseClicked(event -> {
                    System.out.println("OK");
                });
                root.getChildren().add(box);
                box.getTransforms().addAll(new Translate(x * BOX_PADDING, y * BOX_PADDING, 0));
                animateSphere(box, x * BOX_PADDING, y * BOX_PADDING, x * 200);
            }
        }

        PointLight light = new PointLight();

        light.getTransforms().addAll(
                new Translate(75, 200, -200)
        );

        root.getChildren().addAll(light);

        // Use a SubScene
        SubScene subScene = new SubScene(root, 1200, 1200);
        subScene.setCamera(camera);
        subScene.setFill(Color.SKYBLUE);
        subScene.setPickOnBounds(true);
        Group group = new Group();
        group.getChildren().addAll(subScene);

        this.getChildren().addAll(group, new Text("this is a test for 2d"));
    }

    private static void animateSphere(Box box, int x, int y, int millis) {
        Translate radiusTranslate = new Translate(x, y, 0);
        Translate zMovement = new Translate();

        box.getTransforms().setAll(zMovement, radiusTranslate);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(zMovement.zProperty(), 0d)),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(zMovement.zProperty(), 1d, Interpolator.EASE_BOTH))
        );

        tl.setCycleCount(Timeline.INDEFINITE);
        tl.setAutoReverse(true);
        tl.setDelay(new Duration(millis));
        tl.play();
    }

    private String buildEnemyBoxId(int x, int y) {
        return GRID_BOX + x + " " + y;
    }

    private String buildEnemyBoxId(Coordinate coordinate) {
        return GRID_BOX + coordinate.getX() + " " + coordinate.getY();
    }

    private String buildPlayerBoxId(int x, int y) {
        return GRID_BOX + (x + BOX_PADDING) + " " + (y + BOX_PADDING);
    }

    private String buildPlayerBoxId(Coordinate coordinate) {
        return GRID_BOX + (coordinate.getX() + BOX_PADDING) + " " + (coordinate.getY() + BOX_PADDING);
    }
}
