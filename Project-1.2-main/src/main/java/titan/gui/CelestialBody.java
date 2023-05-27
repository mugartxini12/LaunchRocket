package titan.gui;

import javafx.scene.shape.Circle;

public class CelestialBody {

    private final String name;
    private final double posX;
    private final double posY;
    private final Circle celestialBody;

    public CelestialBody(String name, double posX, double posY, int radius) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        celestialBody = new Circle(posX, posY, radius);
    }

    /**
     * Method to return the name of the CelestialBody.
     *
     * @return the String containing the name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to return the X-position of the CelestialBody.
     *
     * @return the x-position
     */
    public double getX() {
        return posX;
    }

    /**
     * Method to return the Y-position of the CelestialBody.
     *
     * @return the y-position
     */
    public double getY() {
        return posY;
    }

    /**
     * Method to return the circle object that represents the CelestialBody.
     *
     * @return the circle object
     */
    public Circle getBody() {
        return celestialBody;
    }
}
