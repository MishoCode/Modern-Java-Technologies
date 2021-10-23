package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Bicycle extends AbstractVehicle {
    public static final double BICYCLE_BOOKING_PRICE = 0.2;
    public static final String BICYCLE_TYPE = "BICYCLE";

    public Bicycle(String id, Location location) {
        super(id, location);
    }

    @Override
    public double getPricePerMinute() {
        return BICYCLE_BOOKING_PRICE;
    }

    @Override
    public String getType() {
        return BICYCLE_TYPE;
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
