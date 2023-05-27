package titan_lander.solver;

import java.util.*;

//class to generate 1D perlin noise

public class Noise {

    private final double[] yPoints;
    private final double[] xPoints;
    private final Random random;
    private final int start;
    private final int end;

    public Noise(double amplitude, int x0, int xEnd, double stepSize) {
        start = x0;
        end = xEnd;
        random = new Random();
        int delta = xEnd - x0;
        yPoints = new double[(int) (delta / stepSize) + 1];
        xPoints = new double[(int) (delta / stepSize) + 1];
        generatePoints(amplitude, start, stepSize);
    }

    //interpolates the value of a function between the points a and b,
    //with 0 <= x <= 1 specifying where between a and b
    //using cosine interpolation
    private double interpolateGraph(double a, double b, double x) {
        double x2 = (1 - Math.cos(x * Math.PI)) / 2.0;
        return (a * (1 - x2) + b * x2);
    }

    //get the value of the function at a point x
    public double getValue(double x) {
        if (x > end || x < start) {
            System.out.println();
            throw new IllegalArgumentException("X : " + x + " is not within range of the function");
        } else {
            double value;
            double a = 0;
            double b = 0;
            double percentage = 0;

            //find the two points that x is between and calculate the percentage where x is located
            for (int i = 0; i < xPoints.length - 1; i++) {
                if (xPoints[i] <= x && x <= xPoints[i + 1]) {
                    a = yPoints[i];
                    b = yPoints[i + 1];
                    percentage = (x - xPoints[i]) / (xPoints[i + 1] - xPoints[i]);
                }
            }
            //interpolate the value and include overtones
            value = interpolateGraph(a, b, percentage);
            return value;
        }
    }

    //returns a random double between -1 and 1
    private double randomDouble() {
        return random.nextDouble() * 2 - 1;
    }

    private void generatePoints(double amp, double x0, double step) {
        for (int i = 0; i < yPoints.length; i++) {
            yPoints[i] = randomDouble() * amp;
            xPoints[i] = x0 + i * step;
        }
    }
}