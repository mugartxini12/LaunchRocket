package titan.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;

import java.util.Timer;
import java.util.TimerTask;

import static titan.gui.Util.startTimerTask;

public class VisualiserScene extends GuiMain {
    public static void setVisualiserScene() {

        Util.setCelestialBodies();
        timeText = new Text();
        timeText.setId("timeText");

        probeLaunch = new Button("Launch Probe!");
        exitButton = new Button("Exit Simulation!");
        landerButtonY = new Button("Observe Landing!");
        landerButtonN = new Button("Return to Earth!");
        landerButtonY.setVisible(false);
        landerButtonN.setVisible(false);

        probeLaunch.setOnAction(e -> {
            startTimerTask();
            for (javafx.animation.Timeline planetPath : planetPaths) {
                PlanetTransition.transition(planetPath);
            }

            Timer scheduler = new Timer();
            scheduler.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (javafx.animation.Timeline planetPath : planetPaths) {
                        PlanetTransition.pauseTransition(planetPath);
                    }
                    landerButtonY.setVisible(true);
                    landerButtonN.setVisible(true);
                }
            }, 26380);
        });

        exitButton.setOnAction(e -> System.exit(0));

        landerButtonY.setOnAction(e -> {
            singleStage.setScene(landerVisualiserScene);
        });

        landerButtonN.setOnAction(e -> {
            for (javafx.animation.Timeline planetPath : planetPaths) {
                PlanetTransition.transition(planetPath);
            }
            landerButtonY.setVisible(false);
            landerButtonN.setVisible(false);
        });

        Slider slider1 = new Slider();

        slider1.setMax(800);
        slider1.setMin(-800);
        slider1.setPrefWidth(300d);
        slider1.setLayoutX(-150);
        slider1.setLayoutY(200);
        slider1.setShowTickLabels(false);
        slider1.setStyle("-fx-base: yellow");

        Translate translate = new Translate();

        slider1.valueProperty().addListener((observable, oldValue, newValue) -> translate.setX((double) newValue));

        Slider slider2 = new Slider();

        slider2.setMax(800);
        slider2.setMin(-800);
        slider2.setPrefWidth(300d);
        slider2.setLayoutX(-150);
        slider2.setLayoutY(200);
        slider2.setShowTickLabels(false);
        slider2.setStyle("-fx-base: yellow");

        slider2.valueProperty().addListener((observable, oldValue, newValue) -> translate.setY((double) newValue));

        for (CelestialBody planetBody : planetBodies) {
            planetBody.getBody().getTransforms().add(translate);
        }

        InfoScreen.run();

        scrollScale = new SimpleDoubleProperty(1.0);
        zoomPane = new Pane();
        zoomPane.scaleXProperty().bind(scrollScale);
        zoomPane.scaleYProperty().bind(scrollScale);

        ObservableList<Node> zoomChildren = zoomPane.getChildren();
        for (CelestialBody planetBody : planetBodies) {
            zoomChildren.add(planetBody.getBody());
        }
        // Initially displayed Solar System position
        zoomPane.setTranslateX(-screenBounds.getWidth() / 4);
        zoomPane.setTranslateY(-screenBounds.getHeight() / 4);

        zoomPane.setOnScroll(scrollHandler);
        zoomPane.setOnMousePressed(pressHandler);
        zoomPane.setOnMouseDragged(dragHandler);

        BorderPane root = new BorderPane();
        root.setRight(infoBox);
        root.setLeft(landerSelection);
        root.setAlignment(timeText, Pos.BOTTOM_RIGHT);
        root.setBottom(slider1);
        root.setTop(slider2);
        root.setCenter(zoomPane);
        // Make the infoBox always appear on top in relation to the zoomPane
        // This allows the the buttons 'probeLaunch' and 'exitButton' to be clickable at all times.
        zoomPane.toBack();

        visualiserScene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        visualiserScene.getStylesheets().add("Stylesheet.css");
    }
}
