package titan.gui;

import titan.solver.Solver;

import java.util.Timer;
import java.util.TimerTask;

public class Util extends GuiMain {
    /**
     * Sets all the celestial bodies to be seen in the visualiser scene.
     */
    public static void setCelestialBodies() {

        distancePixel = 3000 / screenBounds.getHeight();

        planetBodies[0] = new CelestialBody("Sun", centerX + (((-6.806783239281648e8) / (1e9)) / distancePixel), centerY - (((1.080005533878725e9) / (1e9)) / distancePixel), 15);
        planetBodies[0].getBody().getStyleClass().add("sun");

        planetBodies[1] = new CelestialBody("Mercury", centerX + (((6.047855986424127e6) / (1e9)) / distancePixel), centerY - (((-6.801800047868888e10) / (1e9)) / distancePixel), 6);
        planetBodies[1].getBody().getStyleClass().add("mercury");

        planetBodies[2] = new CelestialBody("Venus", centerX + (((-9.435345478592035e10) / (1e9)) / distancePixel), centerY - (((5.350359551033670e10) / (1e9)) / distancePixel), 7);
        planetBodies[2].getBody().getStyleClass().add("venus");

        planetBodies[3] = new CelestialBody("Earth", centerX + (((-1.471922101663588e11) / (1e9)) / distancePixel), centerY - (((-2.860995816266412e10) / (1e9)) / distancePixel), 11);
        planetBodies[3].getBody().getStyleClass().add("earth");

        planetBodies[4] = new CelestialBody("Moon", centerX + (((-1.472343904597218e11) / (1e9)) / distancePixel), centerY - (((-2.822578361503422e10) / (1e9)) / distancePixel), 5);
        planetBodies[4].getBody().getStyleClass().add("moon");

        planetBodies[5] = new CelestialBody("Mars", centerX + (((-3.615638921529161e10) / (1e9)) / distancePixel), centerY - (((-2.167633037046744e11) / (1e9)) / distancePixel), 7);
        planetBodies[5].getBody().getStyleClass().add("mars");

        planetBodies[6] = new CelestialBody("Jupiter", centerX + (((1.781303138592153e11) / (1e9)) / distancePixel), centerY - (((-7.551118436250277e11) / (1e9)) / distancePixel), 13);
        planetBodies[6].getBody().getStyleClass().add("jupiter");

        planetBodies[7] = new CelestialBody("Saturn", centerX + (((6.328646641500651e11) / (1e9)) / distancePixel), centerY - (((-1.358172804527507e12) / (1e9)) / distancePixel), 13);
        planetBodies[7].getBody().getStyleClass().add("saturn");

        planetBodies[8] = new CelestialBody("Titan", centerX + (((6.332873118527889e11) / (1e9)) / distancePixel), centerY - (((-1.357175556995868e12) / (1e9)) / distancePixel), 6);
        planetBodies[8].getBody().getStyleClass().add("titan");

        planetBodies[9] = new CelestialBody("Neptune", centerX + (((4.382692942729203e12) / (1e9)) / distancePixel), centerY - (((-9.093501655486243e11) / (1e9)) / distancePixel), 15);
        planetBodies[9].getBody().getStyleClass().add(("neptune"));

        planetBodies[10] = new CelestialBody("Uranus", centerX + (((2.395195786685187e12) / (1e9)) / distancePixel), centerY - (((1.744450959214586e12) / (1e9)) / distancePixel), 15);
        planetBodies[10].getBody().getStyleClass().add("uranus");

        planetBodies[11] = new CelestialBody("Probe", centerX + (((-1.471922101663588e11) / (1e9)) / distancePixel) + (0.1 / 1e9), centerY - (((-2.860995816266412e10) / (1e9)) / distancePixel) - (6371e3 / 1e9), 4);
        planetBodies[11].getBody().getStyleClass().add("probe");
    }

    /**
     * Times and updates the probe journey continuously - seconds since launch.
     */
    public static void startTimerTask() {
        if (isTimerTaskRunning) {
            timerTask.cancel();
            isTimerTaskRunning = false;
        }
        timerTime = 0;
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                isTimerTaskRunning = true;
                /* Basically from accessing the list accessTimes, we conclude that it has 525948 elements.
                 * By dividing this value (number of steps taken) by 12 seconds - the set duration of the visualisation -
                 * we get an integer number, since 525948%43829 = 0 (obtained from 525948/12 = 43829).
                 * This allows us to access the list at that same index each time.
                 * This way, we can make the timer increase harmoniously during the entirety of the probe launch.
                 */
                if (timerTime + 43829 <= Solver.getAccessTimes().size() + 1) {
                    timerTime += 43829;
                }
                if (timerTime == 525948) {
                    timerTime -= 2;
                    ssl = "Time Since Launch: " + Solver.getAccessTimes().get(timerTime);
                    timer.cancel();
                }
                ssl = "Time Since Launch: " + Solver.getAccessTimes().get(timerTime);
                timeText.setText(ssl);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }
}
