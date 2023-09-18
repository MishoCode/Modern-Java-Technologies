package bg.sofia.uni.fmi.mjt.race.track;

import java.util.Random;

public class Car implements Runnable {
    private static final int DRIVING_TIME = 500;
    private static final Random RANDOM = new Random();

    private final int id;
    private int nPitStops;
    private final Track track;

    public Car(int id, int nPitStops, Track track) {
        this.id = id;
        this.nPitStops = nPitStops;
        this.track = track;
    }

    @Override
    public void run() {
        while (nPitStops >= 0) {
            try {
                Thread.sleep(RANDOM.nextInt(DRIVING_TIME));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            track.enterPit(this);
            nPitStops--;

        }
    }

    public int getCarId() {
        return id;
    }

    public int getNPitStops() {
        return nPitStops;
    }

    public Track getTrack() {
        return track;
    }
}
