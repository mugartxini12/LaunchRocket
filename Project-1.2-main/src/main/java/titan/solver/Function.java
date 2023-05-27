package titan.solver;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class Function implements ODEFunctionInterface {
    public RateInterface call(double t, StateInterface y) {
        State state = (State) y;
        return new Rate(state.calcAcc(t));
    }
}
