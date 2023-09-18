package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.Random;

public class PitTeam extends Thread {

    private static final Random RANDOM = new Random();
    private static final int MAX_REPAIR_TIME = 200;

    private final int id;
    private final Pit pitStop;
    private int stoppedCarsCount = 0;

    public PitTeam(int id, Pit pitStop) {
        this.id = id;
        this.pitStop = pitStop;
    }

    @Override
    public void run() {
        Car car;

        while ((car = pitStop.getCar()) != null) {
            try {
                Thread.sleep(RANDOM.nextInt(MAX_REPAIR_TIME));
            } catch (InterruptedException e) {
                System.err.println("Unexpected exception was thrown");
                e.printStackTrace();
            }

            stoppedCarsCount++;
        }

        System.out.printf("Pit team %d repaired %d racing cars\n", id, stoppedCarsCount);
    }

    public int getPitStoppedCars() {
        return stoppedCarsCount;
    }
}