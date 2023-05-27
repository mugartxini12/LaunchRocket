package titan.space;

import titan.interfaces.Vector3dInterface;

import java.util.ArrayList;
import java.util.List;

public class Planet {

    protected List<Vector3d> positions;
    protected List<Vector3d> velocities;
    protected String name;
    protected int stateIndex;

    public Planet(String name, Vector3d initialPosition, Vector3d initialVelocity) {
        positions = new ArrayList<>();
        velocities = new ArrayList<>();
        positions.add(initialPosition);
        velocities.add(initialVelocity);
        this.name = name;
        stateIndex = 0;
    }

    private Planet(String name, List<Vector3d> positions, List<Vector3d> velocities, int stateIndex) {
        this.name = name;
        this.positions = positions;
        this.velocities = velocities;
        this.stateIndex = stateIndex;
    }

    protected Planet() {
    }

    public void update(double step, Vector3dInterface acceleration) {
        velocities.add((Vector3d) velocities.get(stateIndex).addMul(step, acceleration));
        positions.add((Vector3d) positions.get(stateIndex).addMul(step, velocities.get(stateIndex + 1)));
        stateIndex++;
    }

    public void addMulPos(double scalar, Vector3dInterface other) {
        Vector3d positionBackup = positions.get(stateIndex).copy();
        positions.set(stateIndex, (Vector3d) positions.get(stateIndex).addMul(scalar * scalar, other));
        velocities.add((Vector3d) positions.get(stateIndex).sub(positionBackup).mul(1 / scalar));
    }

    public void setPosition(Vector3d position, int index) {
        if (index > positions.size() - 1) {
            positions.add(position);
            stateIndex++;
        } else {
            positions.set(index, position);
        }
    }

    public Vector3dInterface getPosition(int index) {
        return positions.get(index);
    }

    public List<Vector3d> getPositions() {
        return positions;
    }

    public Vector3dInterface getVelocity() {
        return velocities.get(stateIndex);
    }

    public Vector3dInterface getVelocity(int index) {
        return velocities.get(index);
    }

    public String toString() {
        return "[ name: " + name + ", pos: " + positions.toString() + " vel: " + velocities.toString() + "]";
    }

    public Planet copy() {
        List<Vector3d> copyPositions = new ArrayList<>();
        for (Vector3d position : positions) {
            copyPositions.add(position.copy());
        }
        List<Vector3d> copyVelocities = new ArrayList<>();
        for (Vector3d velocity : velocities) {
            copyVelocities.add(velocity.copy());
        }
        return new Planet(name, copyPositions, copyVelocities, stateIndex);
    }
}
