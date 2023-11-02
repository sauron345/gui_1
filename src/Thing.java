import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Thing {
    private String name;
    private int area;
    public static List<Thing> thingsToUtilization = new ArrayList<>();
    public static List<Thing> allExistingThings = new ArrayList<>();

    public Thing(String name, int area) {
        this.name = name;
        this.area = area;
    }
    public Thing(String name, int height, int width, int length) {
        this.name = name;
        this.area = height * width * length;
    }

    public static void showThingDetails(Thing selThing) {
        System.out.println("Name: " + selThing.name);
        System.out.println("Area: " + selThing.area);

        if (selThing instanceof Vehicle) {
            System.out.println("Vehicle type: " + ((Vehicle) selThing).getVehicleType());
            System.out.println("Engine type: " + ((Vehicle) selThing).getEngineType());
            System.out.println("Engine capacity: " + ((Vehicle) selThing).getEngineCapacity());
            if (Vehicle.getSoldVehicles().isEmpty())
                System.out.println("Sold vehicles: none");
            else
                System.out.println("Sold vehicles: " + Vehicle.getSoldVehicles().toString());

        } else {
            if (Thing.thingsToUtilization.isEmpty())
                System.out.println("Things to utilization: none");
            else
                System.out.println("Things to utilization: " + Thing.thingsToUtilization.toString());
        }
    }

    public static void selThingOption(int choice, Parking selPlace) {
        if (choice == 15) {
            Place.showPlaceDetails(selPlace);
        } else if (choice == 9)
            Parking.addParkingTenant(selPlace);
        else if (choice == 20)
            Parking.addThing(selPlace);
        else {
            Thing selThing = selPlace.storedThings.get(choice);
            System.out.println("Selected thing: " + selThing.name);
            Thing.showThingDetails(selThing);
        }
    }

    public static Thing findExistingThing() {
        List<String> ThingData = Thing.enterThingData();
        int same = 0;
        for (Thing thing : Thing.allExistingThings) {
            if (thing.name.equals(ThingData.get(0) + " " + ThingData.get(1)))
                same++;
            if (thing.area == Integer.parseInt(ThingData.get(2)))
                same++;
            if (same == 2)
                return thing;
        }
        System.out.println("Entered thing is not exists");
        return null;
    }


    public static Thing selThing(Parking selParking) {
        System.out.println("Stored things in " + selParking.name + "\n");
        for (int i = 0; i < selParking.storedThings.size(); i++) {
            if (selParking.storedThings.get(i) instanceof Vehicle)
                System.out.println(i + " - (Vehicle) " + selParking.storedThings.get(i));
            else
                System.out.println(i + " - " + selParking.storedThings.get(i));
        }
        int choice2 = Main.scan.nextInt();
        return selParking.storedThings.get(choice2);
    }

    public static List<String> enterThingData() {
        System.out.print("Enter thing name: ");
        String name = Main.scan.next();

        System.out.print("Enter thing type (Vehicle or Thing): ");
        String type = Main.scan.next();

        System.out.println("Enter:\n1 - volume\n2 - width, height, length");
        int choice = Main.scan.nextInt();
        List<String> volume = Main.enterVolumeData(choice);

        if (volume.size() > 1)
            return Arrays.asList(name, volume.get(0), volume.get(1), volume.get(2), type);
        else
            return Arrays.asList(name, volume.get(0), type);
    }

    public static Thing createThing() {
        List<String> personData = Thing.enterThingData();

        if (personData.get(2).equals("Thing")) {
            return new Thing(personData.get(0), Integer.parseInt(personData.get(1)));
        } else if (personData.get(2).equals("Vehicle")) {
            List<String> vehicleData = Thing.enterVehicleReqParams();
            if (personData.size() > 5)
                return new Vehicle(personData.get(0), Integer.parseInt(personData.get(1)),
                        Integer.parseInt(personData.get(2)), Integer.parseInt(personData.get(1)),
                        Integer.parseInt(vehicleData.get(0)), vehicleData.get(1),
                        vehicleData.get(2));
            else
                return new Vehicle(personData.get(0), Integer.parseInt(personData.get(1)),
                        Integer.parseInt(vehicleData.get(0)), vehicleData.get(1), vehicleData.get(2));
        }
        return null;
    }

    private static List<String> enterVehicleReqParams() {
        List<String> vehicleData = new ArrayList<>();

        System.out.print("Enter engine capacity: ");
        vehicleData.add(Main.scan.next());

        System.out.print("Enter vehicle type (available: " + Arrays.toString(Vehicle.availableTypes) +"): ");
        vehicleData.add(Main.scan.next());

        System.out.print("Enter engine type: ");
        vehicleData.add(Main.scan.next());

        return vehicleData;

    }

    public String getName() {
        return name;
    }

    public int getArea() {
        return area;
    }

    public static List<Thing> getThingsToUtilization() {
        return thingsToUtilization;
    }

    public static List<Thing> getAllExistingThings() {
        return allExistingThings;
    }
}
