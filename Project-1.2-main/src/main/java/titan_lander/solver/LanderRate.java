package titan_lander.solver;

import titan.interfaces.RateInterface;
import titan.interfaces.Vector3dInterface;
import titan.space.Vector3d;

public class LanderRate implements RateInterface {
    Vector3d acceleration;

    public LanderRate(Vector3dInterface acceleration) {
        this.acceleration = (Vector3d) acceleration;
    }

    public LanderRate add(LanderRate newRate) {

        return new LanderRate(acceleration.add(newRate.getAcceleration()));
    }

    public LanderRate mul(double num) {

        return new LanderRate(acceleration.mul(num));
    }

    public Vector3d getAcceleration() {
        return acceleration;
    }
}
