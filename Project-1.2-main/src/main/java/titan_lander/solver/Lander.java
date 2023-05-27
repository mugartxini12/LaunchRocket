package titan_lander.solver;


import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;
import titan.space.Vector3d;
import titan_lander.interfaces.AbstractLander;
import titan_lander.interfaces.ControllerInterface;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

public class Lander extends AbstractLander {

    private static boolean hasCrashed;
    private static boolean hasLanded;
    private final double error = 0.1;
    private static boolean wasPrinted;
    private static final AirDrag airDrag = new AirDrag(0, 300000);
    private final double mass = 16400;

    public Lander(ControllerInterface controller, Vector3d initialPos, Vector3d initialVel) {
        super(controller, initialPos, initialVel);

    }

    public void update(double step, Vector3d acc) {
        if (hasCrashed) {
            return;
        }
        super.setVelocity((Vector3d) velocity.addMul(step, acc));
        super.setPosition((Vector3d) position.addMul(step, velocity));
    }

    public Vector3d calcAcc(double t) {
        if (hasCrashed) {
            return new Vector3d(0, 0, 0);
        }
        Vector3d controllerAcc = new Vector3d(controller.getX(this, t), controller.getY(this, t), controller.getTheta(this, t));
        Vector3d windAcc = (Vector3d) airDrag.getWindForce(velocity, position.getY()).mul(1 / mass);
        return (Vector3d) controllerAcc.add(windAcc);
    }

    public void hasLanded() {
        boolean xPos = abs(position.getX()) <= 0.1;
        boolean thetaPos = angleWithinError(0.02);
        boolean xVel = abs(velocity.getX()) <= 0.1;
        boolean yVel = abs(velocity.getY()) <= 0.1;
        boolean thetaVel = abs(velocity.getZ()) <= 0.01;
        if (position.getY() <= error) {
            hasCrashed = true;
            if (!wasPrinted) {
                System.out.println("Position: " + position);
                System.out.println("Velocity: " + velocity);
                wasPrinted = true;
            }
            if (xPos & thetaPos & xVel & yVel & thetaVel) {
                if (!hasLanded) {
                    System.out.println("Successful landing!");
                    hasLanded = true;
                }
            }
        }
    }

    @Override
    public StateInterface addMul(double step, RateInterface rate) {
        Lander newLander = (Lander) cloneLander(this);
        newLander.update(step, ((LanderRate) rate).getAcceleration());
        return newLander;
    }

    public StateInterface cloneLander(Lander lander) {
        return new Lander(lander.getController(), lander.getPosition(), lander.getVelocity());
    }

    private boolean angleWithinError(double error) {
        double angle = position.getZ();
        //reduce angle to something less than 2 * PI
        while (angle > 2 * PI) {
            angle -= 2 * PI;
        }
        //if the angle is within the error bounds
        return angle < error || abs(angle - 2 * PI) < error;
    }
}
