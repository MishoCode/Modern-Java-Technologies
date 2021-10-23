package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;

public interface Vehicle {
    double getPricePerMinute();

    String getType();

    String getId();

    Location getLocation();

    LocalDateTime getEndOfReservationPeriod();

    void setEndOfReservationPeriod(LocalDateTime until);
}
