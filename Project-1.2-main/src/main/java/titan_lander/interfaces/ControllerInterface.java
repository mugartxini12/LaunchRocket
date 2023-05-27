package titan_lander.interfaces;

/**
 * ControllerInterface
 */
public interface ControllerInterface {

    double TITAN_G = 1.352;

    //methods to get the accelerations of the lander based on the current state and the time
    double getX(AbstractLander lander, double t);

    double getY(AbstractLander lander, double t);

    double getTheta(AbstractLander lander, double t);

}
