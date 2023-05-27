package titan.space;

import titan.interfaces.Vector3dInterface;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Vector3d implements Vector3dInterface {

    private double x;
    private double y;
    private double z;

    public Vector3d() {
    }

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public Vector3dInterface add(Vector3dInterface other) {
        return new Vector3d(x + other.getX(), y + other.getY(), z + other.getZ());
    }

    @Override
    public Vector3dInterface sub(Vector3dInterface other) {
        return new Vector3d(x - other.getX(), y - other.getY(), z - other.getZ());
    }

    @Override
    public Vector3dInterface mul(double scalar) {
        return new Vector3d(x * scalar, y * scalar, z * scalar);
    }

//    /**
//     * The matrix multiplication
//     * @param num treated as a one-row and one-column matrix
//     * @return
//     */
//    public double vectorMul(double num) {
//        return (this.x * num + this.y * num + this.z * num);
//    }

    @Override
    public Vector3dInterface addMul(double scalar, Vector3dInterface other) {
        return add(other.mul(scalar));
    }

    @Override
    public double norm() {
        return sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2));
    }

    @Override
    public double dist(Vector3dInterface other) {
        return sqrt(pow(x - other.getX(), 2) + pow(y - other.getY(), 2) + pow(z - other.getZ(), 2));
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    public Vector3d copy() {
        return new Vector3d(x, y, z);
    }
}
