package bg.sofia.uni.fmi.mjt.rentalservice.service;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;

public class RentalService implements RentalServiceAPI {
    private final Vehicle[] vehicles;

    private boolean vehicleExists(Vehicle vehicle) {
        for (Vehicle v : vehicles) {
            if (v.equals(vehicle)) {
                return true;
            }
        }

        return false;
    }

    private boolean isBooked(Vehicle vehicle) {
        return vehicle.getEndOfReservationPeriod().isAfter(LocalDateTime.now());
    }

    public RentalService(Vehicle[] vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public double rentUntil(Vehicle vehicle, LocalDateTime until) {
        if (!vehicleExists(vehicle) || isBooked(vehicle) || until.isBefore(LocalDateTime.now())) {
            return -1.0;
        }

        vehicle.setEndOfReservationPeriod(until);
        long minutes = Duration.between(LocalDateTime.now().withNano(0), until.withNano(0)).toMinutes();
        return minutes * vehicle.getPricePerMinute();
    }

    @Override
    public Vehicle findNearestAvailableVehicleInRadius(String type, Location location, double maxDistance) {
        Vehicle nearestVehicle = null;
        double nearestDistance = maxDistance;

        for (Vehicle v : vehicles) {
            double currentDistance = Location.getDistance(v.getLocation(), location);
            if (v.getType().equals(type) && currentDistance < maxDistance) {
                if (!isBooked(v) && currentDistance < nearestDistance) {
                    nearestDistance = currentDistance;
                    nearestVehicle = v;
                }
            }
        }

        return nearestVehicle;
    }
}
