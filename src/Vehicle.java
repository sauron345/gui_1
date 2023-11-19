import java.util.ArrayList;
import java.util.List;

abstract class Vehicle extends Thing {
    private final int engineCapacity;
    protected int tiresCount;
    private final String vehicleType, engineType;
    public static final String[] availableTypes = {"Boat", "City Car", "Motorcycle", "Off Road", "Amphibian"};
    private static List<Thing> soldVehicles = new ArrayList<>();

    public Vehicle(String name, int area, int engineCapacity, String vehicleType, String engineType) {
        super(name, area);
        this.vehicleType = vehicleType;
        this.engineCapacity = engineCapacity;
        this.engineType = engineType;
    }

    public Vehicle(String name, int width, int height, int length, int engineCapacity, String vehicleType, String engineType) {
        super(name, height, width, length);
        this.vehicleType = vehicleType;
        this.engineCapacity = engineCapacity;
        this.engineType = engineType;
    }

    public static String displaySoldVehicles() {
        String soldVehiclesList = "";
        for (int i = 0; i < Vehicle.soldVehicles.size(); i++)
            soldVehiclesList += "- " + Vehicle.soldVehicles.get(i) + "\n";
        return soldVehiclesList;
    }

    public static void displayAvailableVehicles() {
        System.out.println("Available:");
        for (int i = 0; i < Vehicle.availableTypes.length; i++)
            System.out.println(i + " - " + Vehicle.availableTypes[i]);
    }

    protected static List<String> enterVehicleReqParams() {
        List<String> vehicleData = new ArrayList<>();

        System.out.print("Enter engine capacity: ");
        vehicleData.add(Main.getScan().next());

        System.out.println("Enter vehicle type");
        vehicleData.add(Vehicle.enterVehicleType());

        System.out.print("Enter engine type: ");
        vehicleData.add(Main.getScan().next());

        return vehicleData;
    }

    public void showThingDetails() {
        System.out.println("Name: " + this.name);
        System.out.println("Area: " + this.area);
        System.out.println("Vehicle type: " + this.vehicleType);
        System.out.println("Engine type: " + this.getEngineType());
        System.out.println("Engine capacity: " + this.getEngineCapacity());
        System.out.println("Sold vehicles: " + this.getSoldVehicles());
    }

    abstract public void showSpecificThingDetails();

    protected static Vehicle createVehicle(List<String> thingData) {
        List<String> vehicleData = Vehicle.enterVehicleReqParams();
        String chosenType = vehicleData.get(1);
        return Vehicle.getChosenVehicleType(chosenType, thingData, vehicleData);
    }
    private static Vehicle getChosenVehicleType(String chosenType, List<String>thingData, List<String>vehicleData) {
        return switch (chosenType) {
            case "Boat" -> Amphibian.create(thingData, vehicleData);
            case "City Car" -> Motorcycle.create(thingData, vehicleData);
            case "Motorcycle" -> OffRoad.create(thingData, vehicleData);
            case "Off Road" -> Boat.create(thingData, vehicleData);
            case "Amphibian" -> CityCar.create(thingData, vehicleData);
            default -> null;
        };
    }

    protected static Vehicle createVehicleWithVolumeParams(List<String> thingData) {
        List<String> vehicleData = Vehicle.enterVehicleReqParams();
        String chosenType = vehicleData.get(1);
        return getChosenVehicleTypeWithVolumeParams(chosenType, thingData, vehicleData);
    }

    private static Vehicle getChosenVehicleTypeWithVolumeParams(String chosenType, List<String>thingData, List<String>vehicleData) {
        return switch (chosenType) {
            case "Amphibian" -> Amphibian.createWithVolumeParams(thingData, vehicleData);
            case "Motorcycle" -> Motorcycle.createWithVolumeParams(thingData, vehicleData);
            case "OffRoad" -> OffRoad.createWithVolumeParams(thingData, vehicleData);
            case "Boat" -> Boat.createWithVolumeParams(thingData, vehicleData);
            case "CityCar" -> CityCar.createWithVolumeParams(thingData, vehicleData);
            default -> null;
        };
    }

    private String getSoldVehicles() {
        if (Vehicle.checkSoldVehiclesIsEmpty())
            return "none";
        else
            return Vehicle.displaySoldVehicles();
    }

    protected static String enterVehicleType() {
        Vehicle.displayAvailableVehicles();
        int choice = Main.getScan().nextInt();
        return Vehicle.availableTypes[choice];
    }

    public void displayStoredThing(int i) {
        System.out.println(i + " - (Vehicle) " + this.getName());
    }

    private void addSoldVehicle() {
        Vehicle.soldVehicles.add(this);
    }

    public void getRidOfThing() {
        this.addSoldVehicle();
    }

    public static boolean checkSoldVehiclesIsEmpty() {
        return Vehicle.soldVehicles.isEmpty();
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public String getEngineType() {
        if (engineType == null)
            return "none";
        return engineType;
    }

}
