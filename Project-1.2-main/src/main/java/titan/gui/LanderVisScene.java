package titan.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import titan.space.Vector3d;

public class LanderVisScene extends GuiMain {
    public static void setLanderVisualizerScene() {
        landerProbeLaunch = new Button("Launch Lander!");
        landerExitButton = new Button("Exit Simulation!");

        landerProbeLaunch.setOnAction(e -> animateLander());

        landerExitButton.setOnAction(e -> singleStage.setScene(visualiserScene));

        landerButtonBox = new VBox(25);
        landerButtonBox.getChildren().addAll(landerProbeLaunch, landerExitButton);

        drawLanderPath();
        pathLines = new Pane(landerPath);

        BorderPane root = new BorderPane();
        root.setCenter(pathLines);
        root.setRight(landerButtonBox);
        root.getStyleClass().add("landerBackground");

        landerVisualiserScene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        landerVisualiserScene.getStylesheets().add("Stylesheet.css");

    }

    public static void drawLanderPath() {
        MoveTo firstMove = new MoveTo(firstVector.getX(), firstVector.getY());
        landerPath = new Path(firstMove);

        for (int i = 1; i < landerPathVectors.length; i++) {
            Vector3d end = metersToPixels(landerPathVectors[i]);
            LineTo line = new LineTo(end.getX(), end.getY());
            landerPath.getElements().add(line);
        }
    }

    public static void animateLander() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);

        for (int i = 0; i < landerPathVectors.length; i++) {
            Vector3d v = metersToPixels(landerPathVectors[i]);

            KeyValue rotation = new KeyValue(landerView.rotateProperty(), Math.toDegrees(v.getZ()));
            KeyValue xPos = new KeyValue(landerView.xProperty(), v.getX() - imgSize / 2);
            KeyValue yPos = new KeyValue(landerView.yProperty(), v.getY() - imgSize / 2);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(totalAnimTime / (landerFinalTime / landerTimeStep) * i), rotation, xPos, yPos));
        }
        timeline.play();
    }
}

