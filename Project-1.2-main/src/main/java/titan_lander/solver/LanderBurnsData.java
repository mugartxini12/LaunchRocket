package titan_lander.solver;

public class LanderBurnsData {
    private final double engineAcc;
    private final double torque;
    private final double timeStart;
    private final double timeEnd;

    public LanderBurnsData(double engineAcc, double torque, double timeStart, double timeEnd) {
        this.engineAcc = engineAcc;
        this.torque = torque;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public double getEngineAcc() {
        return engineAcc;
    }

    public double getTorque() {
        return torque;
    }

    public boolean isTimeInRange(double time) {
        double epsilon = 0.001;
        return greaterThan(timeEnd, time, epsilon) && (greaterThan(time, timeStart, epsilon) || equals(time, timeStart, epsilon));
    }

    private boolean greaterThan(double a, double b, double epsilon) {
        return a - b > epsilon;
    }

    private boolean equals(double a, double b, double epsilon) {
        return a == b || Math.abs(a - b) < epsilon;
    }

}
