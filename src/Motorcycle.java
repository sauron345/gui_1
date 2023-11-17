import java.util.List;

public class Motorcycle extends Vehicle {
    private final int standSize;

    public Motorcycle(String name, int area, int engineCapacity, String vehicleType, String engineType) {
        super(name, area, engineCapacity, vehicleType, engineType);
        super.tiresCount = 2;
        this.standSize = this.enterStandSize();
    }

    public Motorcycle(String name, int width, int height, int length, int engineCapacity, String vehicleType, String engineType) {
        super(name, width, height, length, engineCapacity, vehicleType, engineType);
        super.tiresCount = 2;
        this.standSize = this.enterStandSize();
    }

    protected static Motorcycle createWithVolumeParams(List<String> thingData, List<String> vehicleData) {
        return new Motorcycle(thingData.get(1), Integer.parseInt(thingData.get(2)),
            Integer.parseInt(thingData.get(3)), Integer.parseInt(thingData.get(4)),
            Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
    }

    protected static Motorcycle create(List<String> thingData, List<String> vehicleData) {
        return new Motorcycle(thingData.get(1), Integer.parseInt(thingData.get(2)),
            Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
    }

    private int enterStandSize() {
        System.out.print("Enter stand size: ");
        return Main.getScan().nextInt();
    }

    public int getStandSize() {
        return standSize;
    }

}
