package bg.sofia.uni.fmi.mjt.rentalservice.service;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

import java.time.LocalDateTime;

public interface RentalServiceAPI {
    double rentUntil(Vehicle vehicle, LocalDateTime until);

    Vehicle findNearestAvailableVehicleInRadius(String type, Location location, double maxDistance);
}
