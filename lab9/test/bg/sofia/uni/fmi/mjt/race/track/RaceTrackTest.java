package bg.sofia.uni.fmi.mjt.race.track;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RaceTrackTest {
    private static Track raceTrack;

    @BeforeAll
    public static void setUp() throws InterruptedException {
        raceTrack = new RaceTrack(20);
        Random random = new Random();
        Thread[] cars = new Thread[100];

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Thread(new Car(i, random.nextInt(4), raceTrack));
            cars[i].start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        raceTrack.getPit().finishRace();

        for (Thread car : cars) {
            car.join();
        }

        for (Thread team : raceTrack.getPit().getPitTeams()) {
            team.join();
        }
    }

    @Test
    public void testGetNumberOfFinishedCars() {
        assertEquals(raceTrack.getNumberOfFinishedCars(), 100);
    }

    @Test
    public void testGetFinishedCarsIds() {
        List<Integer> expected = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            expected.add(i);
        }

        List<Integer> actual = raceTrack.getFinishedCarsIds();

        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }
}
