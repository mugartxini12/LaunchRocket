package titan_lander.solver;

import titan_lander.interfaces.AbstractLander;
import titan_lander.interfaces.ControllerInterface;

import static java.lang.Math.*;

public class OpenLoopController implements ControllerInterface {

    private final double TITAN_G = 1.352;
    double time = 1457.7;
    LanderBurnsData[] landerBurnsData = {
            new LanderBurnsData(0, 0.1 * PI, 1, 2),
            new LanderBurnsData(0, -0.1 * PI, 11, 12),
            new LanderBurnsData(1.9939, 0, 12, 42),
            new LanderBurnsData(0, 0.1 * PI, 42, 43),
            new LanderBurnsData(0, -0.1 * PI, 52, 53),
            new LanderBurnsData(2.7, 0, 304, 319),
            new LanderBurnsData(0, -0.1 * PI, 542, 543),
            new LanderBurnsData(0, 0.1 * PI, 547, 548),
            new LanderBurnsData(2.69172, 0, time, time + 23.7)
    };

    public OpenLoopController() {
    }

    @Override
    public double getX(AbstractLander lander, double t) {
        double u = findU(t);
        return u * sin(lander.getPosition().getZ());
    }

    @Override
    public double getY(AbstractLander lander, double t) {
        double u = findU(t);
        return u * cos(lander.getPosition().getZ()) - TITAN_G;
    }

    @Override
    public double getTheta(AbstractLander lander, double t) { // maybe no need for parameters
        return findV(t);
    }

    private double findU(double t) {
        for (LanderBurnsData landerBurnsDatum : landerBurnsData) {
            if (landerBurnsDatum.isTimeInRange(t)) {
                return landerBurnsDatum.getEngineAcc();
            }
        }
        return 0;
    }

    private double findV(double t) {
        for (LanderBurnsData landerBurnsDatum : landerBurnsData) {
            if (landerBurnsDatum.isTimeInRange(t)) {
                return landerBurnsDatum.getTorque();
            }
        }
        return 0;
    }
}
