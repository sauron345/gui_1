import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Parking extends Place {
    public List<Thing> storedThings = new ArrayList<>();
    public int availableSpace;

    public Parking(String name, int volume) {
        super(name, volume);
        this.availableSpace = volume;
    }

    public Parking(String name, int height, int width, int length) {
        super(name, height, width, length);
        this.availableSpace = volume;
    }

    public void storeThing(Thing thing) throws TooManyThingsException {
        this.availableSpace = currFreeSpace();
        if (thing.getArea() <= this.availableSpace)
            this.storedThings.add(thing);
        else
            throw new TooManyThingsException();
    }

    private int currFreeSpace() {
        if (this.storedThings.size() > 0) {
            for (Thing storeThing : this.storedThings)
                this.availableSpace -= storeThing.getArea();
        } else
            this.availableSpace = this.volume;
        return this.availableSpace;
    }

    public void clearParking() {
        for (Thing storedThing : this.storedThings) {
            if (storedThing instanceof Vehicle)
                Vehicle.getSoldVehicles().add(storedThing);
            else
                Thing.thingsToUtilization.add(storedThing);
            this.storedThings.remove(storedThing);
        }
    }

    public static void addParkingTenant(Parking selPlace) {
        System.out.print("Tenant:\n1 - create new ");
        if (Person.allExistingPersons.size() > 0)
            System.out.println("\n2 - add existing");
        int choice = Main.scan.nextInt();
        Person regPerson;
        if (choice == 1) {
            regPerson = Person.createPerson();
            regPerson.isTenant = true;
            Person.allExistingPersons.add(regPerson);

            System.out.print("Enter person rent end: ");
            String rentEnd = Main.scan.next();
            selPlace.startPlaceRental(regPerson, LocalDate.parse(rentEnd));
        } else if (choice == 2 && Person.allExistingPersons.size() > 0) {
            regPerson = Person.findExistingPerson();
            if (regPerson != null) {
                regPerson.isTenant = true;
                selPlace.startPlaceRental(regPerson, LocalDate.parse("20/12/2003"));
            }
        }
    }

    public static void addThing(Parking selParking) throws TooManyThingsException {
        System.out.print("Thing:\n1 - create new ");
        if (Thing.allExistingThings.size() > 0)
            System.out.print("\n2 - add existing: ");
        int choice = Main.scan.nextInt();
        Thing storeThing;
        if (choice == 1) {
            storeThing = Thing.createThing();
            if (storeThing != null) {
                selParking.storedThings.add(storeThing);
                Thing.allExistingThings.add(storeThing);
            }
        } else if (choice == 2 && Thing.allExistingThings.size() > 0) {
            storeThing = Thing.findExistingThing();
            if (storeThing != null)
                selParking.storeThing(storeThing);
        }
    }

    public static void showStoredThings(Parking selParking) {
        System.out.println("\nSelect thing stored in " + selParking.name + ":");
        System.out.println("15 - Parking details");
        if (selParking.tenant == null)
            System.out.println("9 - Add tenant");
        else if (selParking.storedThings.isEmpty()) {
            System.out.println("Nothing stored yet");
        } else {
            System.out.println("------------------");
            System.out.println("Tenant - " + selParking.tenant.name);
            System.out.println("20 - Add thing");
            for (int i = 0; i < selParking.storedThings.size(); i++)
                System.out.println(i + " - " + selParking.storedThings.get(i).getName());
        }
    }

    public static Parking selParking(Person selTenant) {
        System.out.println("Select parking:\n");
        for (int i = 0; i < selTenant.rentedPlaces.size(); i++)
            if (selTenant.rentedPlaces.get(i) instanceof Parking)
                System.out.println(i + " - " + selTenant.rentedPlaces.get(i));
        int choice = Main.scan.nextInt();

        return (Parking) selTenant.rentedPlaces.get(choice);
    }

    public List<Thing> getStoredThings() {
        return storedThings;
    }

    public int getAvailableSpace() {
        return availableSpace;
    }
}
