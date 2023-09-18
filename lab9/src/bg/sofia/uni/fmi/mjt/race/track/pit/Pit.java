package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Pit {
    private final int nPitTeams;
    private final PitTeam[] pitTeams;
    private final Queue<Car> waitingCars;
    private final AtomicInteger pitStopsCount;
    private boolean isFinished;

    public Pit(int nPitTeams) {
        this.nPitTeams = nPitTeams;
        pitTeams = new PitTeam[nPitTeams];
        waitingCars = new LinkedList<>();
        pitStopsCount = new AtomicInteger(0);
        initPitTeams();
    }

    public void submitCar(Car car) {
        pitStopsCount.incrementAndGet();

        synchronized (this) {
            waitingCars.add(car);
            this.notify();
        }
    }

    public synchronized Car getCar() {
        if (waitingCars.isEmpty() && !isFinished) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return waitingCars.isEmpty() ? null : waitingCars.poll();
    }

    public int getPitStopsCount() {
        return pitStopsCount.get();
    }

    public List<PitTeam> getPitTeams() {
        return Collections.unmodifiableList(Arrays.stream(pitTeams).toList());
    }

    public synchronized void finishRace() {
        isFinished = true;
        this.notifyAll();
    }

    private void initPitTeams() {
        for (int i = 0; i < nPitTeams; i++) {
            pitTeams[i] = new PitTeam(i, this);
            pitTeams[i].setName("Pit" + i);
            pitTeams[i].start();
        }
    }

}
