package titan;

import titan.gui.PlanetTransition;
import titan.interfaces.ProbeSimulatorInterface;
import titan.interfaces.Vector3dInterface;
import titan.solver.Function;
import titan.solver.Solver;
import titan.solver.State;
import titan.space.SolarSystemData;
import titan.space.Vector3d;

public class ProbeSimulator implements ProbeSimulatorInterface {
    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface initialPosition, Vector3dInterface initialVelocity, double[] outputTimes) {
        Vector3dInterface[] trajectory = new Vector3d[outputTimes.length];
        State initialState = new State(initialPosition, initialVelocity);
        Solver solver = new Solver();
        State[] states = (State[]) solver.solve(new Function(), initialState, outputTimes);
        PlanetTransition.addPath(states);
        for (int i = 0; i < states.length; i++) {
            trajectory[i] = states[i].getShuttlePosition();
        }
        return trajectory;
    }

    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface initialPosition, Vector3dInterface initialVelocity, double finalTime, double stepSize) {
        Vector3dInterface[] trajectory = new Vector3d[(int) Math.ceil(finalTime / stepSize) + 1];
        State initialState = new State(initialPosition, initialVelocity);
        System.out.println("Probe starting position: " + initialPosition);
        System.out.println("Probe starting velocity: " + initialVelocity);
        Solver solver = new Solver();
        State[] states = (State[]) solver.solve(new Function(), initialState, finalTime, stepSize);
        PlanetTransition.addPath(states);
        double bestDist = Double.MAX_VALUE;
        double bestTime = 0;
        int bestIndex = -1;
        int planetID = 8;
        for (int i = 0; i < states.length; i++) {
            trajectory[i] = states[i].getShuttlePosition();

            double dist = states[i].getPlanetPosition(planetID).dist(states[i].getShuttlePosition());
            if (dist < bestDist) {
                bestIndex = i;
                bestDist = dist;
                bestTime = i * stepSize;
            }

        }

        System.out.println("Time of closest approach: " + bestTime);
        System.out.println("Distance of closest approach: " + bestDist);
        System.out.println("Shuttle position of closest approach: " + states[bestIndex].getPlanetPosition(planetID));
        System.out.println("Body position of closest approach: " + states[bestIndex].getShuttlePosition());

        if (bestDist <= SolarSystemData.radii[planetID]) {
            System.out.println("Titan has been hit");
        }
        return trajectory;
    }
}
