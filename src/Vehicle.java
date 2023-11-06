import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Vehicle extends Thing {
    private int engineCapacity;
    private String vehicleType, engineType;
    public static final String[] availableTypes = {"Boat", "City Car", "Motorcycle", "Off Road", "Amphibian"};
    private static List<Thing> soldVehicles = new ArrayList<>();

    public Vehicle(String name, int area, int engineCapacity, String vehicleType, String engineType) {
        super(name, area);
        assert Arrays.asList(availableTypes).contains(vehicleType);
        this.vehicleType = vehicleType;
        this.engineCapacity = engineCapacity;
        this.engineType = engineType;
    }

    public Vehicle(String name, int width, int height, int length, int engineCapacity, String vehicleType, String engineType) {
        super(name, height, width, length);
        assert Arrays.asList(availableTypes).contains(vehicleType);
        this.vehicleType = vehicleType;
        this.engineCapacity = engineCapacity;
        this.engineType = engineType;
    }

    public static void addSoldVehicle(Thing thing) {
        Vehicle.soldVehicles.add(thing);
    }

    public static void displaySoldVehicles() {
        System.out.println("Sold vehicles:");
        for (int i = 0; i < Vehicle.soldVehicles.size(); i++) {
            System.out.println("- " + Vehicle.soldVehicles.get(i));
        }
    }

    public static void displayAvailableVehicles() {
        System.out.println("Available:");
        for (int i = 0; i < Vehicle.availableTypes.length; i++) {
            System.out.println("- " + Vehicle.availableTypes[i]);
        }
    }

    public static boolean checkSoldVehiclesIsEmpty() {
        return Vehicle.soldVehicles.isEmpty();
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getEngineType() {
        return engineType;
    }

}
