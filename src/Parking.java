import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Parking extends Place {
    private List<Thing> storedThings = new ArrayList<>();
    private int availableSpace;

    public Parking(String name, int volume) {
        super(name, volume);
        this.availableSpace = volume;
    }

    public Parking(String name, int height, int width, int length) {
        super(name, height, width, length);
        this.availableSpace = height * width * length;
    }

    public void storeThing(Thing thing) throws TooManyThingsException {
        this.availableSpace = currFreeSpace();
        if (thing.getArea() <= this.availableSpace)
            this.storedThings.add(thing);
        else
            throw new TooManyThingsException();
    }

    private int currFreeSpace() {
        if (this.storedThingsSize() > 0) {
            for (Thing storeThing : this.storedThings)
                this.availableSpace -= storeThing.getArea();
        } else
            this.availableSpace = this.getVolume();
        return this.availableSpace;
    }

    public void clearParking() {
        for (Thing storedThing : this.storedThings) {
            if (storedThing instanceof Vehicle)
                Vehicle.addSoldVehicle(storedThing);
            else
                Thing.addThingToUtilization(storedThing);
            this.storedThings.remove(storedThing);
        }
    }

    public static void addParkingTenant(Parking selPlace) {
        System.out.print("Tenant:\n1 - create new ");
        if (Person.allExistingPersonsSize() > 0)
            System.out.println("\n2 - add existing");
        int choice = Main.getScan().nextInt();
        Person regPerson;
        if (choice == 1) {
            regPerson = Person.createPerson();
            Person.addPersonToExisting(regPerson);
            selPlace.tenantConfig(regPerson, selPlace);
        } else if (choice == 2 && Person.allExistingPersonsSize() > 0) {
            regPerson = Person.findExistingPerson();
            if (regPerson != null)
                selPlace.tenantConfig(regPerson, selPlace);
        }
    }

    public static void addThing(Parking selParking) throws TooManyThingsException {
        System.out.print("Thing:\n1 - create new ");
        if (Thing.allExistingThingsSize() > 0)
            System.out.print("\n2 - add existing: ");
        int choice = Main.getScan().nextInt();
        Thing storeThing;
        if (choice == 1) {
            storeThing = Thing.createThing();
            if (storeThing != null) {
                selParking.storedThings.add(storeThing);
                Thing.addThingToExisting(storeThing);
            }
        } else if (choice == 2 && Thing.allExistingThingsSize() > 0) {
            storeThing = Thing.findExistingThing();
            if (storeThing != null)
                selParking.storeThing(storeThing);
        }
    }

    public static void showStoredThings(Parking selParking) {
        System.out.println("\nSelect thing stored in " + selParking.getName() + ":");
        System.out.println("15 - Parking details");
        if (selParking.getTenant() == null)
            System.out.println("9 - Add tenant");
        else {
            System.out.println("20 - Add thing");
            System.out.println("------------------");
            System.out.println("Tenant - " + selParking.getTenant().getName());
            if (selParking.storedThings.isEmpty())
                System.out.println("Nothing stored yet");
            for (int i = 0; i < selParking.storedThingsSize(); i++)
                System.out.println(i + " - " + selParking.storedThings.get(i).getName());
        }

    }

    public static Parking selParking(Person selTenant) {
        if (selTenant.rentedPlacesSize() > 0) {
            System.out.println("Select parking:");
            for (int i = 0; i < selTenant.rentedPlacesSize(); i++)
                if (selTenant.getRentedPlace(i) instanceof Parking)
                    System.out.println(i + " - " + selTenant.getRentedPlace(i));
            int choice = Main.getScan().nextInt();

            return (Parking) selTenant.getRentedPlace(choice);
        }
        System.out.println("You do not rent any parking");
        return null;
    }

    public void displayStoredThings() {
        System.out.print("Stored things:");
        if (this.storedThings.size() > 0)
            for (int i = 0; i < this.storedThings.size(); i++)
                System.out.println("- " + this.storedThings.get(i));
        else
            System.out.println(" not stored yet");
    }

    public Thing getStoreThing(int index) {
        return this.storedThings.get(index);
    }

    public int storedThingsSize() {
        return this.storedThings.size();
    }

    public boolean checkStoredThingExists(Thing thing) {
        return this.storedThings.contains(thing);
    }

    public void removeStoredThing(Thing thing) {
        this.storedThings.remove(thing);
    }

    public int getAvailableSpace() {
        return availableSpace;
    }

}
