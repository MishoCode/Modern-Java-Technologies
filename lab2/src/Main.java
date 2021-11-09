import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.service.RentalService;
import bg.sofia.uni.fmi.mjt.rentalservice.service.RentalServiceAPI;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Bicycle;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Car;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Scooter;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Vehicle car1 = new Car("car-1", new Location(1, 1));
        Vehicle car2 = new Car("car-2", new Location(2, 1));
        Vehicle car3 = new Car("car-3", new Location(5, 3));
        Vehicle car4 = new Car("car-4", new Location(1.2, 1.2));

        Vehicle bicycle1 = new Bicycle("bike-1", new Location(0.5, 0.5));
        Vehicle bicycle2 = new Bicycle("bike-2", new Location(1.5, 0.5));
        Vehicle bicycle3 = new Bicycle("bike-3", new Location(2, 2));
        Vehicle bicycle4 = new Bicycle("bike-4", new Location(0.1, 0.1));

        Vehicle scooter1 = new Scooter("scooter-1", new Location(3, 4));
        Vehicle scooter2 = new Scooter("scooter-2", new Location(5, 6));
        Vehicle scooter3 = new Scooter("scooter-3", new Location(25, 2.5));
        Vehicle scooter4 = new Scooter("scooter-4", new Location(12, 12));

        Vehicle[] vehicles = new Vehicle[] {
            car1,
            car2,
            car3,
            car4,
            bicycle1,
            bicycle2,
            scooter1,
            scooter2,
            scooter3,
            scooter4};

        RentalServiceAPI rentalService = new RentalService(vehicles);

        //LocalDateTime time1 = LocalDateTime.of(2021,10,30,9,5);
        System.out.println("Rent until tests:");

        double priceForCar = rentalService.rentUntil(car1, LocalDateTime.now().plusMinutes(81));
        double priceForBicycle = rentalService.rentUntil(bicycle1, LocalDateTime.now().plusMinutes(20));
        double priceForBicycle2 = rentalService.rentUntil(bicycle1, LocalDateTime.now().plusMinutes(5));
        double priceForBicycle3 = rentalService.rentUntil(bicycle3, LocalDateTime.now().plusMinutes(25));

        System.out.printf("%.2f\n", priceForCar);
        System.out.printf("%.2f\n", priceForBicycle);
        System.out.printf("%.2f\n", priceForBicycle2);
        System.out.printf("%.2f\n", priceForBicycle3);

        Location currentLocation = new Location(0, 0);

        System.out.println("Nearest car tests:");
        Vehicle nearestCar =
            rentalService.findNearestAvailableVehicleInRadius("CAR", currentLocation, 20.5);
        System.out.println(nearestCar.equals(car1)); //false, it is already booked
        System.out.println(nearestCar.equals(car2)); //false
        System.out.println(nearestCar.equals(car3)); //false
        System.out.println(nearestCar.equals(car4)); //true

        System.out.println("Nearest scooter tests:");
        Vehicle nearestScooter1 =
            rentalService.findNearestAvailableVehicleInRadius("SCOOTER", currentLocation, 1.5);
        System.out.println(nearestScooter1 == null); //true
        Vehicle nearestScooter2 =
            rentalService.findNearestAvailableVehicleInRadius("SCOOTER", currentLocation, 100.0);
        System.out.println(nearestScooter2.equals(scooter1)); //true
        System.out.println(nearestScooter2.equals(scooter3)); //false

        System.out.println("Nearest bicycle tests:");
        Vehicle nearestBicycle =
            rentalService.findNearestAvailableVehicleInRadius("BICYCLE", currentLocation, 10.0);
        System.out.println(nearestBicycle.equals(bicycle2)); //true
        System.out.println(nearestBicycle.equals(bicycle4)); //false
    }
}
