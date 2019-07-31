package com.soen6441.battleship.view.gui.scenes.gameplayscene;

import com.soen6441.battleship.services.gameconfig.GameConfig;
import com.soen6441.battleship.view.gui.scenes.IScene;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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

import java.util.Timer;
import java.util.TimerTask;

public class GamePlay3DScene implements IScene {
    @Override
    public Scene buildScene() {
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
            for (int y = 11; y < gridSize + 11; y++) {
                Box box = new Box(10, 10, 10);
                box.setOnMouseClicked(event -> {
                    System.out.println("OK");
                });
                root.getChildren().add(box);
                box.getTransforms().addAll(new Translate(x * 11, y * 11, 0));
                animateSphere(box, x * 11, y * 11, x * 200);
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

        HBox hBox = new HBox();

        hBox.getChildren().addAll(group, new Text("this is a test for 2d"));

        return new Scene(hBox);
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
}
