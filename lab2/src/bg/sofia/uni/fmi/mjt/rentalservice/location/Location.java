package bg.sofia.uni.fmi.mjt.rentalservice.location;

public class Location {
    private final double x;
    private final double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static double getDistance(Location from, Location to) {
        return Math.sqrt(Math.pow(from.getX() - to.getX(), 2) +
                Math.pow(from.getY() - to.getY(), 2));
    }
}
