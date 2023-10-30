import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Vehicle extends Thing {
    public int engineCapacity;
    public String vehicleType, engineType;
    public static final String[] availableTypes = {"Boat", "City Car", "Motorcycle", "Off Road", "Amphibian"};
    public static List<Thing> soldVehicles = new ArrayList<>();

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
