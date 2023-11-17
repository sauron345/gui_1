import java.util.Arrays;
import java.util.List;

public class Amphibian extends Vehicle {
    private final int armorThickness;
    private final String tiresType;
    private final String[] availableTiresTypes = {"caterpillar", "circular"};

    public Amphibian(String name, int area, int engineCapacity, String vehicleType, String engineType) {
        super(name, area, engineCapacity, vehicleType, engineType);
        this.tiresType = this.selTiresType();
        assert Arrays.asList(availableTiresTypes).contains(this.tiresType);
        super.tiresCount = this.setTiresCount();
        this.armorThickness = this.enterArmorThickness();
    }

    public Amphibian(String name, int width, int height, int length, int engineCapacity, String vehicleType, String engineType) {
        super(name, width, height, length, engineCapacity, vehicleType, engineType);
        this.tiresType = this.selTiresType();
        assert Arrays.asList(availableTiresTypes).contains(this.tiresType);
        super.tiresCount = this.setTiresCount();
        this.armorThickness = this.enterArmorThickness();
    }

    protected static Amphibian createWithVolumeParams(List<String> thingData, List<String> vehicleData) {
        return new Amphibian(thingData.get(1), Integer.parseInt(thingData.get(2)),
            Integer.parseInt(thingData.get(3)), Integer.parseInt(thingData.get(4)),
            Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
    }

    protected static Amphibian create(List<String> thingData, List<String> vehicleData) {
        return new Amphibian(thingData.get(1), Integer.parseInt(thingData.get(2)),
            Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
    }

    private int enterArmorThickness() {
        System.out.print("Enter armor thickness: ");
        return Main.getScan().nextInt();
    }

    private String selTiresType() {
        this.showTiresTypes();
        int choice = Main.getScan().nextInt();
        return this.availableTiresTypes[choice];
    }

    private void showTiresTypes() {
        System.out.println("Enter tires type:");
        for (int i = 0; i < this.availableTiresTypes.length; i++)
            System.out.println(i + " - " + this.availableTiresTypes[i]);
    }

    private int setTiresCount() {
        if (this.tiresType.equals("caterpillar"))
            return 12;
        else
            return 8;
    }

    public int getArmorThickness() {
        return armorThickness;
    }

    public String getTiresType() {
        return tiresType;
    }

}
