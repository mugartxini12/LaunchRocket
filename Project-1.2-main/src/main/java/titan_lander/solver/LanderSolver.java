package titan_lander.solver;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.ODESolverInterface;
import titan.interfaces.StateInterface;

public class LanderSolver implements ODESolverInterface {
    // potential problem with extends: the static accessTime variable for GUI

    @Override
    public StateInterface[] solve(ODEFunctionInterface function, StateInterface initialState, double[] outputTimes) {
        StateInterface[] states = new StateInterface[outputTimes.length];
        states[0] = initialState;
        for (int i = 1; i < states.length; i++) {
            states[i] = step(function, outputTimes[i], states[i - 1], outputTimes[i] - outputTimes[i - 1]);
        }
        return states;
    }

    @Override
    public StateInterface[] solve(ODEFunctionInterface function, StateInterface initialState, double finalTime, double stepSize) {
        StateInterface[] states = new Lander[(int) Math.ceil(finalTime / stepSize) + 1];
        states[0] = initialState;
        double time = 0;
        for (int i = 1; i < states.length; i++) {
            states[i] = step(function, time, states[i - 1], stepSize);
            ((Lander) states[i]).hasLanded();
            if ((finalTime - time) / stepSize < 1) {
                time += (finalTime - time) % stepSize;
            } else {
                time += stepSize;
            }
        }
        return states;
    }

    // Runge-Kutta step with new Rate
    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        LanderRate k1 = (LanderRate) f.call(t, y);
        LanderRate k2 = (LanderRate) f.call(t + h / 2, y.addMul(h, k1.mul(0.5)));
        LanderRate k3 = (LanderRate) f.call(t + h / 2, y.addMul(h, k2.mul(0.5)));
        LanderRate k4 = (LanderRate) f.call(t + h, y.addMul(h, k3));
        LanderRate k = k1.add(k2.mul(2).add(k3.mul(2).add(k4)));
        return y.addMul(h, k.mul(1.0 / 6.0));
    }
}
