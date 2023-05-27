package titan.space;

import java.util.ArrayList;
import java.util.List;

public class Shuttle extends Planet {

    private Shuttle(String name, List<Vector3d> positions, List<Vector3d> velocities, int stateIndex) {
        this.name = name;
        this.positions = positions;
        this.velocities = velocities;
        this.stateIndex = stateIndex;
    }

    private static final EngineBurnsData[] engineBurns = new EngineBurnsData[] {
                    new EngineBurnsData(23e6, 0, 500, 20000, new Vector3d(-0.5, -1, 0)),
                    new EngineBurnsData(16.788850e6, 37000000, 37000500, 20000, new Vector3d(103, -182 , 0)),
                    new EngineBurnsData(0.8e6, 100000000, 100000500, 20000, new Vector3d(0, 0 , -1)),
                    new EngineBurnsData(500, 100000500, 100001000, 20000, new Vector3d(-1, 1 , -1)),
                    new EngineBurnsData(2500, 100001000, 100001500, 20000, new Vector3d(-8.7263, 14.56090, -15.5663)),
                    new EngineBurnsData(2500, 100001500, 100002000, 20000, new Vector3d(-2, 7.937403, -9.896875)),
                    new EngineBurnsData(10000, 259781000, 259781500, 20000, new Vector3d(1,0,0)),
                    new EngineBurnsData(7225000, 259796000, 259796500, 20000, new Vector3d(0,0,1)),
                    new EngineBurnsData(5.375e6, 259796500, 259797000, 20000, new Vector3d(0,1.22,0)),
                    new EngineBurnsData(0.10e6,  259823000,  259823500, 20000, new Vector3d(0,-1,0)),
                    new EngineBurnsData(14.99993e6, 260000000, 260000500, 20000, new Vector3d(-0.603,-1,0)),
                    new EngineBurnsData(1.1992e6, 261000500, 261001000, 20000, new Vector3d(0,0,1)),
                    new EngineBurnsData(0.10075e6, 261001000, 261001500, 20000, new Vector3d(16.13,-2.16,0))
            };

    private static final double fuelMass = 1.8e6;

    public Shuttle(Vector3d initialPosition, Vector3d initialVelocity) {
        super("Shuttle", initialPosition, initialVelocity);
    }

    public Vector3d calcEngineAcc(double t) {
        for (EngineBurnsData engineBurn : engineBurns) {
            if (engineBurn.compareTime(t) == 0) { // check if the engine needs to be fired and return the acceleration
                double mass = calcFuelMass(t) + SolarSystemData.masses[11];
                return engineBurn.calcAcceleration(mass);
            }
        }
        return new Vector3d(0, 0, 0); // if no engine is used return no acceleration
    }

    public double calcFuelMass(double t) {
        double burnedFuelMass = 0;
        for (EngineBurnsData engineBurn : engineBurns) {
            if (engineBurn.compareTime(t) >= 0) { // if a burn has been or is being executed
                double time;
                if (engineBurn.compareTime(t) > 0) // entire burn has been executed
                {
                    time = engineBurn.getEndTime() - engineBurn.getStartTime();
                } else {
                    time = t - engineBurn.getStartTime();
                }
                double fuelConsumption = engineBurn.getForce() / engineBurn.getEffExhVel();
                burnedFuelMass += time * fuelConsumption;
            }
        }
        return fuelMass - burnedFuelMass;
    }

    public EngineBurnsData[] getBurnData() {
        return engineBurns;
    }

    public Shuttle copy() {
        List<Vector3d> copyPositions = new ArrayList<>();
        for (Vector3d position : positions) {
            copyPositions.add(position.copy());
        }
        List<Vector3d> copyVelocities = new ArrayList<>();
        for (Vector3d velocity : velocities) {
            copyVelocities.add(velocity.copy());
        }
        return new Shuttle(name, copyPositions, copyVelocities, stateIndex);
    }
}
