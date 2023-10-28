import java.util.Arrays;
import java.util.List;

//implements Amphibian, Boat, CityCar, Motorcycle
class Vehicle extends Thing {
    private int engineCapacity;
    private String vehicleType, engineType;
    private final String[] availableTypes = {"Boat", "City Car", "Motorcycle", "Off Road", "Amphibian"};
    public static List<Thing> soldVehicles;

    public Vehicle(String name, int area, int engineCapacity, String vehicleType, String engineType) {
        super(name, area);
        assert Arrays.asList(availableTypes).contains(vehicleType);
        this.vehicleType = vehicleType;
        this.engineCapacity = engineCapacity;
        this.engineType = engineType;
    }
    public Vehicle(String name, int height, int width, int length, int engineCapacity, String vehicleType, String engineType) {
        super(name, height, width, length);
        assert Arrays.asList(availableTypes).contains(vehicleType);
        this.vehicleType = vehicleType;
        this.engineCapacity = engineCapacity;
        this.engineType = engineType;
    }

}
