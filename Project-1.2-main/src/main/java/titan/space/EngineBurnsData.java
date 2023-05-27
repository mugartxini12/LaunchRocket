package titan.space;

public class EngineBurnsData {
    private final double force;
    private final double startTime;
    private final double endTime;
    private final double effExhVel;
    private final Vector3d dir;

    public EngineBurnsData(double force, double startTime, double endTime, double effExhVel, Vector3d dir) {
        this.force = force;
        this.startTime = startTime;
        this.endTime = endTime;
        this.effExhVel = effExhVel;
        this.dir = dir;
    }

    public double getForce() {
        return force;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public double getEffExhVel() {
        return effExhVel;
    }


    public Vector3d calcAcceleration(double mass) {
        Vector3d unitDir = (Vector3d) dir.mul(1 / dir.norm());
        return (Vector3d) unitDir.mul(force / mass);
    }


    // returns a value of 1 if t > endTime, value of 0 when startTime < t <= endTime and -1 if t <= startTime
    public int compareTime(double t) {
        if (startTime < t && t <= endTime) {
            return 0;
        } else if (t <= startTime) {
            return -1;
        } else {
            return 1;
        }
    }
}
