package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Scooter extends AbstractVehicle {
    public static final double SCOOTER_BOOKING_PRICE = 0.3;
    public static final String SCOOTER_TYPE = "SCOOTER";

    public Scooter(String id, Location location) {
        super(id, location);
    }

    @Override
    public double getPricePerMinute() {
        return SCOOTER_BOOKING_PRICE;
    }

    @Override
    public String getType() {
        return SCOOTER_TYPE;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
