import java.util.List;

public class OffRoad extends Vehicle {
    private final int highSuspensionSize;

    public OffRoad(String name, int area, int engineCapacity, String vehicleType, String engineType) {
        super(name, area, engineCapacity, vehicleType, engineType);
        super.tiresCount = 4;
        this.highSuspensionSize = this.enterHighSuspensionSize();
    }

    public OffRoad(String name, int width, int height, int length, int engineCapacity, String vehicleType, String engineType) {
        super(name, width, height, length, engineCapacity, vehicleType, engineType);
        super.tiresCount = 4;
        this.highSuspensionSize = this.enterHighSuspensionSize();
    }

    protected static OffRoad createWithVolumeParams(List<String> thingData, List<String> vehicleData) {
        return new OffRoad(thingData.get(1), Integer.parseInt(thingData.get(2)),
            Integer.parseInt(thingData.get(3)), Integer.parseInt(thingData.get(4)),
            Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
    }

    protected static OffRoad create(List<String> thingData, List<String> vehicleData) {
        return new OffRoad(thingData.get(1), Integer.parseInt(thingData.get(2)),
            Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
    }

    @Override
    public void showSpecificThingDetails() {
        super.showThingDetails();
        System.out.println("High suspension size: " + this.highSuspensionSize);
    }

    private int enterHighSuspensionSize() {
        System.out.print("Enter size of high suspension: ");
        return Main.getScan().nextInt();
    }

    public int getHighSuspensionSize() {
        return highSuspensionSize;
    }

}
