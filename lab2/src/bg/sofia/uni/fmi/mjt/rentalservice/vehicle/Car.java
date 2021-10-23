package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Car extends AbstractVehicle {
    public static final double CAR_BOOKING_PRICE = 0.5;
    public static final String CAR_TYPE = "CAR";

    public Car(String id, Location location) {
        super(id, location);
    }

    @Override
    public double getPricePerMinute() {
        return CAR_BOOKING_PRICE;
    }

    @Override
    public String getType() {
        return CAR_TYPE;
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
