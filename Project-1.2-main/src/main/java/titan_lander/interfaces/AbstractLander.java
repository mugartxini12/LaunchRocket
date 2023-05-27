package titan_lander.interfaces;

import titan.interfaces.StateInterface;
import titan.space.Vector3d;

/**
 * class to represent the state of the lander
 */
public abstract class AbstractLander implements StateInterface {
    protected Vector3d position; // stores (x,y,theta)
    protected Vector3d velocity; // stores (x-velocity,y-velocity,angular velocity), e.g. (x_dot,y_dot,theta_dot)
    protected final ControllerInterface controller;

    public AbstractLander(ControllerInterface controller, Vector3d initialPos, Vector3d initialVel) {
        position = initialPos;
        velocity = initialVel;
        this.controller = controller;
    }

    /**
     * Update the lander's position and velocity using acceleration
     *
     * @param step The time-step of the update
     * @param acc  the acceleration applied on the lander
     */
    public abstract void update(double step, Vector3d acc);


    /**
     * Calculate the acceleration of the lander at a specific time
     *
     * @param t The time to evaluate the acceleration
     * @return a vector containing the x-acceleration, y-acceleration and the angular acceleration
     */
    public abstract Vector3d calcAcc(double t);

    public Vector3d getPosition() {
        return position;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    protected void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    protected void setPosition(Vector3d position) {
        this.position = position;
    }

    public ControllerInterface getController() {
        return controller;
    }
}