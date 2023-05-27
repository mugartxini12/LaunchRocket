package titan.solver;

import titan.interfaces.Vector3dInterface;
import titan.space.Vector3d;
import titan.interfaces.RateInterface;

public class Rate implements RateInterface {
    Vector3d[] acceleration;

    public Rate(Vector3d[] acceleration) {
        this.acceleration = acceleration;
    }

    public Rate add(Rate newRate) {
        Vector3dInterface[] newAccelerations = new Vector3d[acceleration.length];
        for (int i = 0; i < acceleration.length; i++) {
            newAccelerations[i] = acceleration[i].add(newRate.getAcceleration()[i]);
        }
        return new Rate((Vector3d[]) newAccelerations);
    }

    public Rate mul(double num) {
        Vector3dInterface[] newAccelerations = new Vector3d[acceleration.length];
        for (int i = 0; i < acceleration.length; i++) {
            newAccelerations[i] = acceleration[i].mul(num);
        }
        return new Rate((Vector3d[]) newAccelerations);
    }

    public Vector3d[] getAcceleration() {
        return acceleration;
    }
}
