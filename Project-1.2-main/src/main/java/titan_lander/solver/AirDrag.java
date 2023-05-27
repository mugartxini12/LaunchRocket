package titan_lander.solver;

public class AirDrag extends Wind {

    public AirDrag(double deltaFactor, int maxHeight) {
        super(deltaFactor, maxHeight);
    }

    @Override
    protected double getWindAtAltitude(double altitude) {//remove the wind so only air drag is calculated
        return 0;
    }
}
