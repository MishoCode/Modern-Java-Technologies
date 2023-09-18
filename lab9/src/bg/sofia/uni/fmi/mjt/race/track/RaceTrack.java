package bg.sofia.uni.fmi.mjt.race.track;

import bg.sofia.uni.fmi.mjt.race.track.pit.Pit;

import java.util.LinkedList;
import java.util.List;

public class RaceTrack implements Track {

    private final int teamsCount;
    private final List<Car> finishedCars;
    private final Pit pit;

    public RaceTrack(int teamsCount) {
        this.teamsCount = teamsCount;
        finishedCars = new LinkedList<>();
        pit = new Pit(teamsCount);
    }

    @Override
    public void enterPit(Car car) {
        if (car.getNPitStops() == 0) {
            synchronized (this) {
                finishedCars.add(car);
            }
            return;
        }

        pit.submitCar(car);
    }

    @Override
    public synchronized int getNumberOfFinishedCars() {
        return finishedCars.size();
    }

    @Override
    public synchronized List<Integer> getFinishedCarsIds() {
        return finishedCars.stream()
            .map(Car::getCarId)
            .toList();
    }

    @Override
    public Pit getPit() {
        return pit;
    }
}
