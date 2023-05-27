package titan_lander.solver;

import titan_lander.interfaces.AbstractLander;
import titan_lander.interfaces.ControllerInterface;

import static java.lang.Math.*;

public class ClosedLoopController implements ControllerInterface {

    private final double maxLanderVel = 30.0;
    private final double maxThetaVel = 0.05 * PI;
    private final double landingHeight = 22215;//from this height the lander will try to land, not caring about x position and velocity
    private final double maxEngineA = 2.7;
    private final double standardError = 0.01;
    private double currentU;

    public ClosedLoopController() {
    }

    @Override
    public double getX(AbstractLander lander, double t) {
        currentU = findU(lander);
        return currentU * sin(lander.getPosition().getZ());
    }

    @Override
    public double getY(AbstractLander lander, double t) {
        return currentU * cos(lander.getPosition().getZ()) - TITAN_G;
    }

    @Override
    public double getTheta(AbstractLander lander, double t) {
        return findV(lander);
    }


    // Also the initial factor that is assigned honestly depends on the frequency we call the controller - like a time-step maybe
    // (aka the frequency we want to adjust the landing - i.e get feedback and come up with new values based on that)
    private double findU(AbstractLander lander) {

        double velFactor = Math.min(2, abs(lander.getPosition().getX() / 6520) + 0.01);
        double minVel = maxLanderVel * velFactor;
        double accFactor = Math.min(1, abs(lander.getPosition().getY() / 50) + 0.01);
        double engineAcc = maxEngineA * accFactor;
        // when close to ground try to land
        if (abs(lander.getPosition().getY()) <= landingHeight) {
            double maxA = maxEngineA * cos(lander.getPosition().getZ()) - TITAN_G;
            double v0 = lander.getVelocity().getY();
            double dist = (v0 * v0) / (2 * maxA) + 0.1;

            if (lander.getPosition().getY() < dist && angleWithinError(lander.getPosition().getZ(), standardError, 0)) {
                return maxEngineA;
            } else {
                return 0;
            }

            // Negative x value, out of goal
        } else if (abs(lander.getPosition().getX()) > 0.1 && lander.getPosition().getX() < 0) {
            if (lander.getVelocity().getX() < minVel) {

                if (angleWithinError(lander.getPosition().getZ(), standardError, PI / 2)) {
                    return engineAcc;
                } else {
                    return 0;
                }
            } else {
                if (angleWithinError(lander.getPosition().getZ(), standardError, 3 * PI / 2)) {
                    return engineAcc;
                } else {
                    return 0;
                }
            }

            // Positive x value, out of goal
        } else if (abs(lander.getPosition().getX()) > 0.1 && lander.getPosition().getX() > 0) {
            if (lander.getVelocity().getX() > -minVel) {
                if (angleWithinError(lander.getPosition().getZ(), standardError, 3 * PI / 2)) {
                    return engineAcc;
                } else {
                    return 0;
                }
            } else {
                if (angleWithinError(lander.getPosition().getZ(), standardError, PI / 2)) {
                    return engineAcc;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

    private double findV(AbstractLander lander) {

        double accFactor = Math.min(1, abs(lander.getPosition().getY() / 400) + 0.01);
        double torque = 0.05 * PI * accFactor;
        double targetAngle;
        double factor = Math.min(5, abs(lander.getPosition().getX() / 997) + 0.01);
        double minVel = maxLanderVel * factor;
        // when close to ground try to land
        if (abs(lander.getPosition().getY()) <= landingHeight) {
            double maxA = torque - TITAN_G;
            double v0 = lander.getVelocity().getY();
            double dist = (v0 * v0) / (2 * maxA) * 1.2;

            targetAngle = 0;
            if (lander.getPosition().getY() < dist && angleWithinError(lander.getPosition().getZ(), standardError, targetAngle)) {
                return 0;
            } else {
                return returnV(lander, targetAngle, torque);
            }

            // Negative x value, out of goal
        } else if (abs(lander.getPosition().getX()) > 0.1 && lander.getPosition().getX() < 0) {
            if (lander.getVelocity().getX() < minVel) {
                targetAngle = PI / 2;
            } else {
                targetAngle = 3 * PI / 2;
            }
            return returnV(lander, targetAngle, torque);


        } else if (abs(lander.getPosition().getX()) > 0.1 && lander.getPosition().getX() > 0) {
            if (lander.getVelocity().getX() > -minVel) {
                targetAngle = 3 * PI / 2;

            } else {
                targetAngle = PI / 2;
            }
            return returnV(lander, targetAngle, torque);
        }
        return 0;
    }

    private boolean angleWithinError(double angle, double error, double target) {
        //reduce angle to something less than 2 * PI
        while (angle >= 2 * PI) {
            angle -= 2 * PI;
        }

        while (angle <= -2 * PI) {
            angle += 2 * PI;
        }
        //if the angle is within the error bounds
        return abs(abs(angle) - target) < error;
    }

    private double returnV(AbstractLander lander, double targetAngle, double torque) {
        if (angleWithinError(lander.getPosition().getZ(), standardError, targetAngle)) {
            return 0;
        } else {
            if (lander.getPosition().getZ() < targetAngle) {
                if (lander.getVelocity().getZ() >= maxThetaVel) {
                    return 0;
                }
                return torque;
            } else {
                if (lander.getVelocity().getZ() <= -maxThetaVel) {
                    return 0;
                }
                return -torque;
            }
        }
    }
}
