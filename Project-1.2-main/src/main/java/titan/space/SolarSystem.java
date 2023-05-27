package titan.space;

import titan.interfaces.Vector3dInterface;

import java.util.ArrayList;
import java.util.List;

public class SolarSystem {

    private static SolarSystem INSTANCE;

    private List<Planet> planets = new ArrayList<>();


    private SolarSystem(Vector3dInterface initialPosition, Vector3dInterface initialVelocity) {
        planets.add(new Planet("Sun", new Vector3d(-6.806783239281648e+08, 1.080005533878725e+09, 6.564012751690170e+06), new Vector3d(-1.420511669610689e+01, -4.954714716629277e+00, 3.994237625449041e-01)));//sun
        planets.add(new Planet("Mercury", new Vector3d(6.047855986424127e+06, -6.801800047868888e+10, -5.702742359714534e+09), new Vector3d(3.892585189044652e+04, 2.978342247012996e+03, -3.327964151414740e+03)));//mercury
        planets.add(new Planet("Venus", new Vector3d(-9.435345478592035e+10, 5.350359551033670e+10, 6.131453014410347e+09), new Vector3d(-1.726404287724406e+04, -3.073432518238123e+04, 5.741783385280979e-04)));//venus
        planets.add(new Planet("Earth", new Vector3d(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06), new Vector3d(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01)));//earth
        planets.add(new Planet("Moon", new Vector3d(-1.472343904597218e+11, -2.822578361503422e+10, 1.052790970065631e+07), new Vector3d(4.433121605215677e+03, -2.948453614110320e+04, 8.896598225322805e+01)));//moon
        planets.add(new Planet("Mars", new Vector3d(-3.615638921529161e+10, -2.167633037046744e+11, -3.687670305939779e+09), new Vector3d(2.481551975121696e+04, -1.816368005464070e+03, -6.467321619018108e+02)));//mars
        planets.add(new Planet("Jupiter", new Vector3d(1.781303138592153e+11, -7.551118436250277e+11, -8.532838524802327e+08), new Vector3d(1.255852555185220e+04, 3.622680192790968e+03, -2.958620380112444e+02)));//jupiter
        planets.add(new Planet("Saturn", new Vector3d(6.328646641500651e+11, -1.358172804527507e+12, -1.578520137930810e+09), new Vector3d(8.220842186554890e+03, 4.052137378979608e+03, -3.976224719266916e+02)));//saturn
        planets.add(new Planet("Titan", new Vector3d(6.332873118527889e+11, -1.357175556995868e+12, -2.134637041453660e+09), new Vector3d(3.056877965721629e+03, 6.125612956428791e+03, -9.523587380845593e+02)));//titan
        planets.add(new Planet("Neptune", new Vector3d(4.382692942729203e+12, -9.093501655486243e+11, -8.227728929479486e+10), new Vector3d(1.068410720964204e+03, 5.354959501569486e+03, -1.343918199987533e+02)));//neptune
        planets.add(new Planet("Uranus", new Vector3d(2.395195786685187e+12, 1.744450959214586e+12, -2.455116324031639e+10), new Vector3d(-4.059468635313243e+03, 5.187467354884825e+03, 7.182516236837899e+01)));//uranus
        planets.add(new Shuttle((Vector3d) initialPosition.add(planets.get(3).getPosition(0)), (Vector3d) initialVelocity.add(planets.get(3).getVelocity(0))));
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public static SolarSystem getInstance(Vector3dInterface initialPosition, Vector3dInterface initialVelocity) {
        if (INSTANCE == null) {
            INSTANCE = new SolarSystem(initialPosition, initialVelocity);
            return INSTANCE;
        }
        return INSTANCE;
    }

    public Shuttle getShuttle() {
        return (Shuttle) planets.get(11);
    }

    public Vector3d[] calcAcc(double t, int stateIndex) {
        Vector3d[] accelerations = new Vector3d[planets.size()];
        for (int i = 0; i < accelerations.length; i++) {
            accelerations[i] = new Vector3d(0, 0, 0);
            for (int j = 0; j < planets.size() - 1; j++) {
                if (i == j) {
                    continue;
                }
                Vector3d deltaPos = (Vector3d) planets.get(j).getPosition(stateIndex).sub(planets.get(i).getPosition(stateIndex));
                Vector3d distanceCubed = (Vector3d) deltaPos.mul(Math.pow(1 / deltaPos.norm(), 3));
                accelerations[i] = (Vector3d) accelerations[i].add(distanceCubed.mul(SolarSystemData.masses[j]));
            }
            final double G = 6.67408e-11;
            accelerations[i] = (Vector3d) accelerations[i].mul(G);
        }
        accelerations[11] = (Vector3d) accelerations[11].add(((Shuttle) planets.get(11)).calcEngineAcc(t));
        return accelerations;
    }

    public Planet get(int index) {
        return planets.get(index);
    }

    public int size() {
        return planets.size();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Planet planet : planets) {
            output.append(planet.toString());
        }
        return output.toString();
    }

    public static void reset(Vector3d initialPosition, Vector3d initialVelocity) {
        INSTANCE = new SolarSystem(initialPosition, initialVelocity);
    }
}
