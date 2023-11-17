import java.util.List;

public class CityCar extends Vehicle {
    private final String spoilerShape;

    public CityCar(String name, int area, int engineCapacity, String vehicleType, String engineType) {
        super(name, area, engineCapacity, vehicleType, engineType);
        super.tiresCount = 4;
        this.spoilerShape = enterSpoilerShape();
    }

    public CityCar(String name, int width, int height, int length, int engineCapacity, String vehicleType, String engineType) {
        super(name, width, height, length, engineCapacity, vehicleType, engineType);
        super.tiresCount = 4;
        this.spoilerShape = enterSpoilerShape();
    }

    protected static Vehicle createWithVolumeParams(List<String> thingData, List<String> vehicleData) {
        return new CityCar(thingData.get(1), Integer.parseInt(thingData.get(2)),
            Integer.parseInt(thingData.get(3)), Integer.parseInt(thingData.get(4)),
            Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
    }

    protected static Vehicle create(List<String> thingData, List<String> vehicleData) {
        return new CityCar(thingData.get(1), Integer.parseInt(thingData.get(2)),
            Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
    }

    private String enterSpoilerShape() {
        System.out.print("Enter spoiler shape: ");
        return Main.getScan().next();
    }

    public String getSpoilerShape() {
        return spoilerShape;
    }

}
