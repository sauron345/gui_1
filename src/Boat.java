import java.util.List;

public class Boat extends Vehicle {
    private final int oarsNumber;
    private final String materialType;

    public Boat(String name, int area, int engineCapacity, String vehicleType, String engineType) {
        super(name, area, 0, vehicleType, null);
        super.tiresCount = 0;
        this.materialType = this.enterMaterialType();
        this.oarsNumber = this.enterOarsNumber();
    }

    public Boat(String name, int width, int height, int length, int engineCapacity, String vehicleType, String engineType) {
        super(name, width, height, length, 0, vehicleType, null);
        super.tiresCount = 0;
        this.materialType = this.enterMaterialType();
        this.oarsNumber = this.enterOarsNumber();
    }

    protected static Vehicle createWithVolumeParams(List<String> thingData, List<String> vehicleData) {
        return new Boat(thingData.get(1), Integer.parseInt(thingData.get(2)),
            Integer.parseInt(thingData.get(3)), Integer.parseInt(thingData.get(4)),
            Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
    }

    protected static Vehicle create(List<String> thingData, List<String> vehicleData) {
        return new Boat(thingData.get(1), Integer.parseInt(thingData.get(2)),
            Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
    }

    @Override
    public void showSpecificThingDetails() {
        super.showThingDetails();
        System.out.println("Oars number: " + this.oarsNumber);
        System.out.println("Material type: " + this.materialType);
    }

    private int enterOarsNumber() {
        System.out.print("Enter oars number: ");
        return Main.getScan().nextInt();
    }

    private String enterMaterialType() {
        System.out.print("Enter material type: ");
        return Main.getScan().next();
    }

    public int getOarsNumber() {
        return oarsNumber;
    }

    public String getMaterialType() {
        return materialType;
    }

}
