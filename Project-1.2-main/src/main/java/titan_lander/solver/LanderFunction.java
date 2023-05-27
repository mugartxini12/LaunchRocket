package titan_lander.solver;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class LanderFunction implements ODEFunctionInterface {

    @Override
    public RateInterface call(double t, StateInterface y) {
        Lander lander = (Lander) y;
        return new LanderRate(lander.calcAcc(t));
    }
}
