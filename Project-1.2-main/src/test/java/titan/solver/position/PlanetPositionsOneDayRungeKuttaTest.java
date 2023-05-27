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

class PlanetPositionsOneDayRungeKuttaTest {

    private static final List<Vector3d> positionsAfterOneDay = new ArrayList<>();
    private static final Solver solver = new Solver();
    private static final double finalTime = 86400;
    private static final double stepSize = 500;
    private final double ACCURACY = 49800000;
    private static final Vector3d initialPosition = new Vector3d(-6371e3, 0.1, 0.1);
    private static final Vector3d initialVelocity = new Vector3d(0, 0, 0);
    private static final State initialState = new State(initialPosition, initialVelocity);
    private static State[] states;
    private static double biggestDifference = 0;
    private static int count = 0;

    @BeforeAll
    static void setUp() {
        states = (State[]) solver.rungeKuttaSolve(new Function(), initialState, finalTime, stepSize);
        SolarSystem.reset(initialPosition,initialVelocity);
        setPositionsAfterOneDay();
    }

    @Test
    void rungeSolverSun() {
        checkPositionAfterOneDay(0);
    }

    @Test
    void rungeSolverMercury() {
        checkPositionAfterOneDay(1);
    }

    @Test
    void rungeSolverVenus() {
        checkPositionAfterOneDay(2);
    }

    @Test
    void rungeSolverEarth() {
        checkPositionAfterOneDay(3);
    }

    @Test
    void rungeSolverMoon() {
        checkPositionAfterOneDay(4);
    }

    @Test
    void rungeSolverMars() {
        checkPositionAfterOneDay(5);
    }

    @Test
    void rungeSolverJupiter() {
        checkPositionAfterOneDay(6);
    }

    @Test
    void rungeSolverSaturn() {
        checkPositionAfterOneDay(7);
    }

    @Test
    void rungeSolverTitan() {
        checkPositionAfterOneDay(8);
    }

    @Test
    void rungeSolverNeptune() {
        checkPositionAfterOneDay(9);
    }

    @Test
    void rungeSolverUranus() {
        checkPositionAfterOneDay(10);
    }

    private void checkPositionAfterOneDay(int i) {
        Vector3dInterface expectedPosition = positionsAfterOneDay.get(i);
        Vector3dInterface actualPosition = states[(int) Math.ceil(finalTime / stepSize)].getPlanetPosition(i);
        double difference = expectedPosition.dist(actualPosition);
        System.out.println(difference / 1000);
        if (difference > biggestDifference) {
            biggestDifference = difference;
            count++;
            System.out.println("Biggest Difference: " + (biggestDifference / 1000) + " count: " + count);
        }
        assertEquals(difference, 0, ACCURACY);
    }

    public static void setPositionsAfterOneDay() {
        /* Sun */
        positionsAfterOneDay.add(new Vector3d(-6.820120542072291E+08, 1.079531639242061E+09, 6.612314219571243E+06));
        /* Mercury */
        positionsAfterOneDay.add(new Vector3d(3.366437797005420E+09, -6.765788029127140E+10, -5.981613930315590E+09));
        /* Venus */
        positionsAfterOneDay.add(new Vector3d(-9.580765049120940E+10, 5.082756749251280E+10, 6.178628634731360E+09));
        /* Earth */
        positionsAfterOneDay.add(new Vector3d(-1.467017349489420E+11, -3.113778902611650E+10, 8.350045664343980E+06));
        /* Moon */
        positionsAfterOneDay.add(new Vector3d(-1.468279160935010E+11, -3.077869284539200E+10, 1.810196579355930E+07));
        /* Mars */
        positionsAfterOneDay.add(new Vector3d(-3.401072563872990E+10, -2.169101963086090E+11, -3.743376828999530E+09));
        /* Jupiter */
        positionsAfterOneDay.add(new Vector3d(1.792150768229270E+11, -7.547979970571840E+11, -8.788450263412590E+08));
        /* Saturn */
        positionsAfterOneDay.add(new Vector3d(6.335748551935350E+11, -1.357822481323310E+12, -1.612884301640510E+09));
        /* Titan */
        positionsAfterOneDay.add(new Vector3d(6.335303511347730E+11, -1.356727039565490E+12, -2.173112534852080E+09));
        /* Neptune */
        positionsAfterOneDay.add(new Vector3d(4.382785256683060E+12, -9.088874998513250E+11, -8.228892955386560E+10));
        /* Uranus */
        positionsAfterOneDay.add(new Vector3d(2.394844929367040E+12, 1.744899234369480E+12, -2.454507287331430E+10));
    }

}