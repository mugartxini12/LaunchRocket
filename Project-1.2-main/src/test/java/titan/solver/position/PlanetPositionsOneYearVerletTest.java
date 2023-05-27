package titan.solver.position;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import titan.interfaces.Vector3dInterface;
import titan.solver.Function;
import titan.solver.Solver;
import titan.solver.State;
import titan.space.SolarSystem;
import titan.space.Vector3d;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlanetPositionsOneYearVerletTest {

    private static final List<Vector3d> positionsAfterOneYear = new ArrayList<>();
    private static final Solver solver = new Solver();
    private static final double finalTime = 31536000;
    private static final double stepSize = 500;
    private final double ACCURACY = 1520000000;
    private static final Vector3d initialPosition = new Vector3d(-6371e3, 0.1, 0.1);
    private static final Vector3d initialVelocity = new Vector3d(0, 0, 0);
    private static final State initialState = new State(initialPosition, initialVelocity);
    private static State[] states;
    private static double biggestDifference = 0;
    private static int count = 0;

    @BeforeAll
    static void setUp() {

        states = (State[]) solver.solve(new Function(), initialState, finalTime, stepSize);
        SolarSystem.reset(initialPosition,initialVelocity);
        setPositionsAfterOneYear();
    }

    @Test
    void verletSolverSun() {
        checkPositionAfterOneYear(0);
    }

    @Test
    void verletSolverMercury() {
        checkPositionAfterOneYear(1);
    }

    @Test
    void verletSolverVenus() {
        checkPositionAfterOneYear(2);
    }

    @Test
    void verletSolverEarth() {
        checkPositionAfterOneYear(3);
    }

    @Test
    void verletSolverMoon() {
        checkPositionAfterOneYear(4);
    }

    @Test
    void verletSolverMars() {
        checkPositionAfterOneYear(5);
    }

    @Test
    void verletSolverJupiter() {
        checkPositionAfterOneYear(6);
    }

    @Test
    void verletSolverSaturn() {
        checkPositionAfterOneYear(7);
    }

    @Test
    void verletSolverTitan() {
        checkPositionAfterOneYear(8);
    }

    @Test
    void verletSolverNeptune() {
        checkPositionAfterOneYear(9);
    }

    @Test
    void verletSolverUranus() {
        checkPositionAfterOneYear(10);
    }

    private void checkPositionAfterOneYear(int i) {
        Vector3dInterface expectedPosition = positionsAfterOneYear.get(i);
        Vector3dInterface actualPosition = states[(int) Math.ceil(finalTime / stepSize)].getPlanetPosition(i);
        double difference = expectedPosition.dist(actualPosition);
        System.out.println(difference / 1000);
        if (difference > biggestDifference) {
            biggestDifference = difference;
            count++;
            System.out.println("Biggest Difference: " + (biggestDifference / 1000) + " count: " + count);
        }
        assertEquals(0, difference, ACCURACY);
    }

    public static void setPositionsAfterOneYear() {
        /* Sun */
        positionsAfterOneYear.add(new Vector3d(-1.082901546284580E+09, 8.147721163745780E+08, 1.863744464428420E+07));
        /* Mercury */
        positionsAfterOneYear.add(new Vector3d(3.950842791655450E+10, -4.720264660931200E+10, -7.628642664193490E+09));
        /* Venus */
        positionsAfterOneYear.add(new Vector3d(1.037791016188340E+11, 2.825331531868220E+10, -5.655906754344890E+09));
        /* Earth */
        positionsAfterOneYear.add(new Vector3d(-1.477129564888790E+11, -2.821064238692060E+10, 2.033107664331600E+07));
        /* Moon */
        positionsAfterOneYear.add(new Vector3d(-1.479155194643600E+11, -2.851137900054520E+10, 2.937109212716480E+07));
        /* Mars */
        positionsAfterOneYear.add(new Vector3d(-8.471846525675170E+10, 2.274355520083190E+11, 6.819319353400900E+09));
        /* Jupiter */
        positionsAfterOneYear.add(new Vector3d(5.299051386136680E+11, -5.398930284706270E+11, -9.615587266077510E+09));
        /* Saturn */
        positionsAfterOneYear.add(new Vector3d(8.778981939996120E+11, -1.204478262290760E+12, -1.400829719307180E+10));
        /* Titan */
        positionsAfterOneYear.add(new Vector3d(8.789384100968740E+11, -1.204002291074610E+12, -1.435729928774680E+10));
        /* Neptune */
        positionsAfterOneYear.add(new Vector3d(4.413142564759280E+12, -7.398714862901390E+11, -8.646907932385070E+10));
        /* Uranus */
        positionsAfterOneYear.add(new Vector3d(2.261199091016790E+12, 1.903465617218640E+12, -2.222474353840990E+10));
    }

}