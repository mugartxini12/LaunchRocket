package titan.gui;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import titan.ProbeSimulator;
import titan.interfaces.StateInterface;
import titan.interfaces.Vector3dInterface;
import titan.space.Vector3d;
import titan_lander.interfaces.ControllerInterface;
import titan_lander.solver.*;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.PI;

public class GuiMain extends Application {

    private final boolean useOpenLoop = true;

    static CelestialBody[] planetBodies = new CelestialBody[12];
    static Rectangle2D screenBounds;
    protected static Stage singleStage;
    public static Scene introScene, visualiserScene, landerIntroScene, landerVisualiserScene;
    public static Button beginButton, landerBeginButton, probeLaunch, landerProbeLaunch, exitButton, landerExitButton, landerButtonY, landerButtonN;
    public static double centerX, centerY, landerCenterX, landerCenterY, distancePixel;
    public static Timeline[] planetPaths = new Timeline[12];
    public static VBox infoBox, landerSelection, landerButtonBox;
    public static HBox[] planetContainers = new HBox[12];
    private static Vector3dInterface initialPosition, initialVelocity;

    public static Vector3d[] landerPathVectors;
    static final double CONVERSION_FACTOR = 3.2000e-03;
    public static Pane pathLines;
    public static Path landerPath;
    public static Vector3d firstVector;
    public static Image landerSprite;
    public static ImageView landerView;
    public static final double imgSize = 75;


    // Timer Related Variables
    protected static TimerTask timerTask;
    protected static Text timeText;
    protected static Timer timer;
    protected static String ssl;
    protected static boolean isTimerTaskRunning;
    protected static int timerTime;

    //Animation time related variables
    protected static double keyTime;
    protected static final double finalTime = 2.95217E8;
    protected static final double timeStep = 500;
    public static final double totalAnimTime = 30 * 1000;
    public static final double landerFinalTime = 2000;
    public static final double landerTimeStep = 0.1;

    // Zoom Related Variables
    protected static DoubleProperty scrollScale;
    protected static Pane zoomPane;
    private static double initialSceneX, initialSceneY, xTranslation, yTranslation;


    public static void main(String[] args) {
        initialPosition = new Vector3d(-6371e3, 0.1, 0.1);
        initialVelocity = new Vector3d(0, 0, 0); // new Vector3d(18044.44, -29351.0, -819.35);
        double MaxAnimationTimeInMillis = 30000;
        double timeSteps = finalTime / timeStep;
        keyTime = MaxAnimationTimeInMillis / timeSteps;
        launch(args);
    }

    public static Vector3dInterface getInitialPosition() {
        return initialPosition;
    }

    public static Vector3dInterface getInitialVelocity() {
        return initialVelocity;
    }

    @Override
    public void start(Stage primaryStage) {

        singleStage = primaryStage;
        singleStage.setTitle("A Titanic Space Odyssey!");

        screenBounds = Screen.getPrimary().getBounds();

        centerX = screenBounds.getWidth() / 2;
        centerY = screenBounds.getHeight() / 2;
        landerCenterX = centerX;
        landerCenterY = landerCenterX;

        // Sets all the scenes that will be (eventually) seen.
        IntroScene.setIntroScene();
        VisualiserScene.setVisualiserScene();

        //get the path of the lander from the simulation:

        //temporary solution until we made the simulation work
        LanderSolver solver = new LanderSolver();
        LanderFunction function = new LanderFunction();
        ControllerInterface controller;
        Vector3d landerInitialVelocity;
        if (useOpenLoop) {
            controller = new OpenLoopController();
            landerInitialVelocity = new Vector3d(0, 0, 0);
        } else {
            controller = new ClosedLoopController();
            landerInitialVelocity = new Vector3d(-10, 0, 0);
        }
        Vector3d landerInitialPosition = new Vector3d(1.3626e+04, 159600, PI / 2);
        StateInterface lander = new Lander(controller, landerInitialPosition, landerInitialVelocity);
        landerPathVectors = getPathVectors(solver.solve(function, lander, landerFinalTime, landerTimeStep));

        firstVector = metersToPixels(landerPathVectors[0]);

        LanderIntroScene.setLanderIntroScene();
        LanderVisScene.setLanderVisualizerScene();

        landerSprite = new Image("lander.png");
        landerView = new ImageView(landerSprite);
        setLanderVector(landerPathVectors[0]);
        landerView.setPreserveRatio(true);
        landerView.setFitHeight(imgSize);

        pathLines.getChildren().add(landerView);

        PlanetTransition.createPath();
        (new ProbeSimulator()).trajectory(initialPosition, initialVelocity, finalTime, timeStep);
        singleStage.setFullScreen(true);
        singleStage.setResizable(false);
        singleStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        singleStage.setScene(introScene);
        singleStage.show();
    }

    /**
     * Handles scrolling related events.
     */
    protected static final EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {

        final double MAX_SCROLL_SCALE = 5.0, MIN_SCROLL_SCALE = 0.7;
        double scaleChanger, xChanger, yChanger;

        @Override
        public void handle(ScrollEvent e) {

            double currentScale = scrollScale.get();
            double prevScale = currentScale;
            double delta = 1.25;

            if (e.getDeltaY() >= 0) {
                currentScale *= delta;
            } else if (e.getDeltaY() < 0) {
                currentScale /= delta;
            }

            currentScale = adjust(currentScale);

            scaleChanger = (currentScale / prevScale) - 1;
            xChanger = (e.getSceneX() - (screenBounds.getWidth() / 2 + screenBounds.getMinX()));
            yChanger = (e.getSceneY() - (screenBounds.getHeight() / 2 + screenBounds.getMinY()));

            scrollScale.set(currentScale);

            zoomPane.setTranslateX(zoomPane.getTranslateX() - scaleChanger * xChanger);
            zoomPane.setTranslateY(zoomPane.getTranslateY() - scaleChanger * yChanger);
        }

        // Prevents going over the scale bounds that were set above.
        private double adjust(double scale) {
            if (scale < MIN_SCROLL_SCALE) {
                return MIN_SCROLL_SCALE;
            }
            return Math.min(scale, MAX_SCROLL_SCALE);
        }
    };

    /**
     * Handles panning related events.
     */
    protected static final EventHandler<MouseEvent> pressHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent e) {
            initialSceneX = e.getSceneX();
            initialSceneY = e.getSceneY();

            xTranslation = zoomPane.getTranslateX();
            yTranslation = zoomPane.getTranslateY();
        }

    };
    protected static final EventHandler<MouseEvent> dragHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent e) {
            zoomPane.setTranslateX(xTranslation + e.getSceneX() - initialSceneX);
            zoomPane.setTranslateY(yTranslation + e.getSceneY() - initialSceneY);
        }
    };

    // Lander path visualiser methods, will create a window and show the lander at each position including the previous path.
    private Vector3d[] getPathVectors() {
        Vector3d[] output = new Vector3d[1000];
        double height = 200000;
        for (int i = 0; i < output.length; i++) {
            output[i] = new Vector3d(0, height - (Math.pow(1.3, i)), i);
        }
        return output;
    }

    private Vector3d[] getPathVectors(StateInterface[] states) {
        Vector3d[] path = new Vector3d[states.length];
        for (int i = 0; i < states.length; i++) {
            path[i] = ((Lander) states[i]).getPosition();
        }
        return path;
    }

    public static Vector3d metersToPixels(Vector3d pos) {
        Vector3d newVector = new Vector3d(landerCenterX + pos.getX() * CONVERSION_FACTOR, landerCenterY - pos.getY() * CONVERSION_FACTOR, pos.getZ());
        return newVector;
    }

    public static void setLanderVector(Vector3d state) {
        Vector3d pixelColor = metersToPixels(state);
        landerView.setRotate(Math.toDegrees(pixelColor.getZ()));
        landerView.setX(pixelColor.getX() - imgSize / 2);
        landerView.setY(pixelColor.getY() - imgSize / 2);
    }
}
