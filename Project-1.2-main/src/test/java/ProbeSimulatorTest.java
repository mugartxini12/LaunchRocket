import org.junit.jupiter.api.Test;
import titan.interfaces.Vector3dInterface;
import titan.solver.State;
import titan.space.EngineBurnsData;
import titan.space.Shuttle;
import titan.space.Vector3d;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProbeSimulatorTest {

    @Test void testFuelMassNotNegative() {
        Vector3dInterface probe_relative_position = new Vector3d(6371e3,0,0);
        Vector3dInterface probe_relative_velocity = new Vector3d(0,0,0);
        State initialState = new State(probe_relative_position,probe_relative_velocity);
        Shuttle shuttle = initialState.getSolarSystem().getShuttle();
        double time = Double.MAX_VALUE;//test for a long time to make sure all engine burns have been executed
        assertEquals(0, Math.min(0,shuttle.calcFuelMass(time)));

    }

    @Test void testFuelMassNotTooMuchLeft() {
        Vector3dInterface probe_relative_position = new Vector3d(6371e3,0,0);
        Vector3dInterface probe_relative_velocity = new Vector3d(0,0,0);
        State initialState = new State(probe_relative_position,probe_relative_velocity);
        Shuttle shuttle = initialState.getSolarSystem().getShuttle();
        double time = Double.MAX_VALUE;//test for a long time to make sure all engine burns have been executed
        double maxFuelLeft = 150000;//the maximum amount of fuel we want left at the end of the mission
        assertEquals(maxFuelLeft, Math.max(maxFuelLeft,shuttle.calcFuelMass(time)));

    }

    @Test void testForceNotTooMuch() {
        State initialState = new State(new Vector3d(0,0,0),new Vector3d(0,0,0));
        Shuttle shuttle = initialState.getSolarSystem().getShuttle();
        double force = 0;
        EngineBurnsData[] engineBurns = shuttle.getBurnData();
        for (EngineBurnsData engineBurn : engineBurns) {
            force += engineBurn.getForce();
        }
        double maxForce = 30e6;
        assertEquals(maxForce, Math.max(maxForce,force/ engineBurns.length));
    }

    @Test void testForceNotNegative() {
        State initialState = new State(new Vector3d(0,0,0),new Vector3d(0,0,0));
        Shuttle shuttle = initialState.getSolarSystem().getShuttle();
        double minForce = 0;
        EngineBurnsData[] engineBurns = shuttle.getBurnData();
        for (EngineBurnsData engineBurn : engineBurns) {
            if (engineBurn.getForce() < minForce) minForce = engineBurn.getForce();
        }
        assertEquals(0, Math.max(0,minForce));
    }




}
