package titan.solver;

import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;
import titan.interfaces.Vector3dInterface;
import titan.space.Planet;
import titan.space.Shuttle;
import titan.space.SolarSystem;
import titan.space.Vector3d;

import java.util.List;

public class State implements StateInterface {

    public static SolarSystem solarSystem;
    private int stateIndex;
    private final Vector3dInterface initialPosition;
    private final Vector3dInterface initialVelocity;

    public State(Vector3dInterface initialPosition, Vector3dInterface initialVelocity) {
        stateIndex = 0;
        solarSystem = SolarSystem.getInstance(initialPosition, initialVelocity);
        this.initialVelocity = initialVelocity;
        this.initialPosition = initialPosition;
    }

    public State newState() {
        State newState = new State(initialPosition, initialVelocity);
        newState.stateIndex = this.stateIndex + 1;
        return newState;
    }

    public StateInterface addMul(double step, RateInterface rate) {
        State nextState = new State(initialPosition, initialVelocity);
        Rate r = (Rate) rate;
        Vector3d[] acceleration = r.getAcceleration();
        for (int i = 0; i < solarSystem.size(); i++) {
            solarSystem.get(i).update(step, acceleration[i]);
        }
        nextState.stateIndex = this.stateIndex + 1;
        return nextState;
    }

    public Shuttle getShuttle() {
        return solarSystem.getShuttle();
    }

    public List<Planet> getPlanets() {
        return solarSystem.getPlanets();
    }

    public Vector3dInterface getPlanetPosition(int planetIndex) {
        return solarSystem.get(planetIndex).getPosition(stateIndex);
    }

    public Vector3dInterface getShuttlePosition() {
        return solarSystem.getShuttle().getPosition(stateIndex);
    }

    public Vector3d[] calcAcc(double t) {
        return solarSystem.calcAcc(t, stateIndex);
    }

    public void addPosition(int planetIndex, Vector3dInterface position) {
        solarSystem.get(planetIndex).setPosition((Vector3d) position, stateIndex);
    }

    public String toString() {
        return "Solar system: " + solarSystem.toString();
    }

    public SolarSystem getSolarSystem() {
        return solarSystem;
    }
}
