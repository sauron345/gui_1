import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Thing {
    protected String name;
    protected int area;
    private static List<Thing> thingsToUtilization = new ArrayList<>();
    private static List<Thing> allExistingThings = new ArrayList<>();

    public Thing(String name, int area) {
        this.name = name;
        this.area = area;
    }
    public Thing(String name, int height, int width, int length) {
        this.name = name;
        this.area = height * width * length;
    }

    public void showSpecificThingDetails() {
        System.out.println("Name: " + this.name);
        System.out.println("Area: " + this.area);
        System.out.println("Things to utilization: " + this.getSoldVehicles());
    }

    private String getSoldVehicles() {
        if (Vehicle.checkSoldVehiclesIsEmpty())
            return "none";
        else
            return Thing.displayThingsToUtilization();
    }

    public void getRidOfThing() {
        this.addToUtilization();
    }

    private static String displayThingsToUtilization() {
        String thingsToUtilizationList = "";
        for (int i = 0; i < Thing.thingsToUtilization.size(); i++)
            thingsToUtilizationList += "- " + Thing.thingsToUtilization.get(i) + "\n";
        return thingsToUtilizationList;
    }


    public void displayStoredThing(int i) {
        System.out.println(i + " - " + this.getName());
    }

    public static List<String> enterThingData() {
        System.out.print("Enter thing name: ");
        String name = Main.getScan().next();

        System.out.println("Enter thing type:\n1 - Vehicle\n2 - Thing");
        String type = Thing.selThingType();

        System.out.println("Enter:\n1 - volume\n2 - width, height, length");
        int choice = Main.getScan().nextInt();
        List<String> volume = Main.enterVolumeData(choice);

        if (volume.size() > 1)
            return Arrays.asList(type, name, volume.get(0), volume.get(1), volume.get(2));
        else
            return Arrays.asList(type, name, volume.get(0), "", "");
    }

    public static Thing createThing() {
        List<String> thingData = Thing.enterThingData();
        String chosenThingType = thingData.get(0);
        return getChosenThingType(chosenThingType, thingData);
    }

    private static Thing getChosenThingType(String chosenThingType, List<String> thingData){
        if (chosenThingType.equals("Thing") && thingData.get(3).equals("") && thingData.get(4).equals(""))
            return Thing.createThing(thingData);
        else if (chosenThingType.equals("Thing"))
            return Thing.createThingWithVolumeParams(thingData);
        else if (chosenThingType.equals("Vehicle") && thingData.get(3).equals("") && thingData.get(4).equals(""))
            return Vehicle.createVehicle(thingData);
        else if (chosenThingType.equals("Vehicle"))
            return Vehicle.createVehicleWithVolumeParams(thingData);

        return null;
    }

    private static String selThingType() {
        int choice = Main.getScan().nextInt();
        if (choice == 1)
            return "Vehicle";
        else if (choice == 2)
            return "Thing";
        else
            return "none";
    }

    private static Thing createThing(List<String> thingData) {
        return new Thing(thingData.get(1), Integer.parseInt(thingData.get(2)));
    }

    private static Thing createThingWithVolumeParams(List<String> thingData) {
        return new Thing(thingData.get(1), Integer.parseInt(thingData.get(2)),
                Integer.parseInt(thingData.get(3)), Integer.parseInt(thingData.get(4)));
    }

    public void addToUtilization() {
        Thing.thingsToUtilization.add(this);
    }

    public static void addThingToExisting(Thing thing) {
        Thing.allExistingThings.add(thing);
    }

    public static int allExistingThingsSize() {
        return Thing.allExistingThings.size();
    }

    public String getName() {
        return name;
    }

    public int getArea() {
        return area;
    }

}
