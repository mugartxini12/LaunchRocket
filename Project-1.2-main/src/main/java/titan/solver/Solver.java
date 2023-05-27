package titan.solver;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.ODESolverInterface;
import titan.interfaces.StateInterface;
import titan.space.Planet;
import titan.space.SolarSystem;
import titan.space.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class Solver implements ODESolverInterface {

    // List to be accessed in the timer functionality in the GuiMain.
    public static List<Double> accessTimes = new ArrayList<>();

    @Override
    public StateInterface[] solve(ODEFunctionInterface function, StateInterface initialState, double[] outputTimes) {
        StateInterface[] states = new StateInterface[outputTimes.length];
        states[0] = initialState;
        states[1] = step(function, outputTimes[1], initialState, 60);
        for (int i = 2; i < states.length; i++) {
            states[i] = verletStep(function, outputTimes[i], states[i - 2], states[i - 1], outputTimes[i] - outputTimes[i - 1]);
            accessTimes.add(outputTimes[i]);
        }
        return states;
    }


    @Override
    public StateInterface[] solve(ODEFunctionInterface function, StateInterface initialState, double finalTime, double stepSize) {
        StateInterface[] states = new State[(int) Math.ceil(finalTime / stepSize) + 1];
        states[0] = initialState;
        double time = 0;
        // bootstrap to get the last 2 positions using Runge-Kutta Solver
        states[1] = step(function, time, initialState, stepSize);
        time = time + stepSize;
        for (int i = 2; i < states.length; i++) {
            states[i] = verletStep(function, time, states[i - 2], states[i - 1], stepSize);
            if ((finalTime - time) / stepSize < 1) {
                time += (finalTime - time) % stepSize;
            } else {
                time += stepSize;
            }
            accessTimes.add(time);
        }
        return states;
    }

    public StateInterface[] rungeKuttaSolve(ODEFunctionInterface function, StateInterface initialState, double finalTime, double stepSize) {
        StateInterface[] states = new State[(int) Math.ceil(finalTime / stepSize) + 1];
        states[0] = initialState;
        double time = 0;
        for (int i = 1; i < states.length; i++) {
            states[i] = step(function, time, states[i - 1], stepSize);
            if ((finalTime - time) / stepSize < 1) {
                time += (finalTime - time) % stepSize;
            } else {
                time += stepSize;
            }
            accessTimes.add(time);
        }
        return states;
    }

    // Runge-Kutta Step
    public StateInterface step(ODEFunctionInterface function, double time, StateInterface state, double stepSize) {
        List<Planet> planetsBackup = getPlanetsBackup((State) state);
        Rate k1 = (Rate) function.call(time, state);
        Rate k2 = (Rate) function.call(time + stepSize / 2, state.addMul(stepSize, k1.mul(0.5)));
        State.solarSystem.setPlanets(planetsBackup);
        planetsBackup = getPlanetsBackup((State) state);
        Rate k3 = (Rate) function.call(time + stepSize / 2, state.addMul(stepSize, k2.mul(0.5)));
        State.solarSystem.setPlanets(planetsBackup);
        planetsBackup = getPlanetsBackup((State) state);
        Rate k4 = (Rate) function.call(time + stepSize, state.addMul(stepSize, k3));
        Rate k = k1.add(k2.mul(2).add(k3.mul(2).add(k4)));
        State.solarSystem.setPlanets(planetsBackup);
        return state.addMul(stepSize, k.mul(1.0 / 6.0));
    }

    private List<Planet> getPlanetsBackup(State state) {
        List<Planet> planetsBackup = new ArrayList<>();
        List<Planet> planets = state.getPlanets();
        for (int i = 0, planetsSize = planets.size() - 1; i < planetsSize; i++) {
            Planet planet = planets.get(i);
            planetsBackup.add(planet.copy());
        }
        planetsBackup.add(state.getShuttle().copy());
        return planetsBackup;
    }

    // Verlet solver implementation
    public StateInterface verletStep(ODEFunctionInterface f, double t, StateInterface previousState, StateInterface currentState, double h) {

        State nextState = ((State) currentState).newState();
        SolarSystem solarSystem = ((State) currentState).getSolarSystem();

        // 2y - lastY + a * h^2
        for (int i = 0; i < solarSystem.size(); i++) {
            nextState.addPosition(i, ((State) currentState).getPlanetPosition(i).mul(2).sub(((State) previousState).getPlanetPosition(i)));
        }

        Rate r = (Rate) f.call(t, currentState);
        Vector3d[] acceleration = r.getAcceleration();
        for (int i = 0; i < solarSystem.size(); i++) {
            solarSystem.get(i).addMulPos(h, acceleration[i]);
        }
        return nextState;
    }

    public static List<Double> getAccessTimes() {
        return accessTimes;
    }
}
