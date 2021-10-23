package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class AbstractVehicle implements Vehicle {
    protected String id;
    protected Location location;
    protected LocalDateTime until;

    protected AbstractVehicle(String id, Location location) {
        this.id = id;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractVehicle that = (AbstractVehicle) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public LocalDateTime getEndOfReservationPeriod() {
        return until == null ? LocalDateTime.now() : until;
    }

    public void setEndOfReservationPeriod(LocalDateTime until) {
        this.until = until;
    }

    public abstract double getPricePerMinute();

    public abstract String getType();

    public abstract String getId();

    public abstract Location getLocation();
}
