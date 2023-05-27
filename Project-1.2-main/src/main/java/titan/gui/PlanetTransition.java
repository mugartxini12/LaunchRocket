package titan.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import titan.solver.State;
import titan.space.SolarSystem;
import titan.space.Vector3d;

import java.util.List;

public class PlanetTransition extends GuiMain {

    public static void createPath() {
        instantiatePaths();
        addAllStartPos();
    }

    private static void addAllStartPos() {
        for (int i = 0; i < planetPaths.length ; i++) {
            addStartPos(planetPaths[i], planetBodies[i]);
        }
    }

    public static void addStartPos(Timeline planetPath, CelestialBody planet) {
        Circle body = planet.getBody();
        KeyValue X = new KeyValue(body.centerXProperty(),planet.getX());
        KeyValue Y = new KeyValue(body.centerYProperty(),planet.getY());
        planetPath.getKeyFrames().add(new KeyFrame(Duration.ZERO,X,Y));
    }

    private static void instantiatePaths() {
        for (int i = 0; i < planetPaths.length; i++) {
            planetPaths[i] = new Timeline();
        }
    }


    public static void addPath(State[] states) {
        SolarSystem solar = states[0].getSolarSystem();//only one instance of the solar system
        for (int q = 0; q < planetPaths.length; q++)
        {
            ObservableList<KeyFrame> keyframes = planetPaths[q].getKeyFrames();
            List<Vector3d> positions = solar.get(q).getPositions();
            System.out.println("Planet: " + q);
            //due to performance reasons we can add all time steps to the animation
            for (int i = 0; i < positions.size(); i += 100)
            {
                Vector3d v = positions.get(i);
                KeyValue X = new KeyValue(planetBodies[q].getBody().centerXProperty(),centerX + (v.getX() / 1e9) / distancePixel);
                KeyValue Y = new KeyValue(planetBodies[q].getBody().centerYProperty(),centerY - (v.getY() / 1e9) / distancePixel);
                keyframes.add(new KeyFrame(Duration.millis(i * keyTime),X,Y));
            }
        }
    }

    public static void transition(Timeline nodePath) {
        nodePath.setCycleCount(1);
        nodePath.setAutoReverse(false);
        nodePath.play();
    }

    public static void pauseTransition(Timeline nodePath) {
        nodePath.pause();
    }
}
