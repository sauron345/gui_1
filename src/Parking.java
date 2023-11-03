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
        if (this.storedThings.size() > 0) {
            for (Thing storeThing : this.storedThings)
                this.availableSpace -= storeThing.getArea();
        } else
            this.availableSpace = this.getVolume();
        return this.availableSpace;
    }

    public void clearParking() {
        for (Thing storedThing : this.storedThings) {
            if (storedThing instanceof Vehicle)
                Vehicle.getSoldVehicles().add(storedThing);
            else
                Thing.addThingToUtilization(storedThing);
            this.storedThings.remove(storedThing);
        }
    }

    public static void addParkingTenant(Parking selPlace) {
        System.out.print("Tenant:\n1 - create new ");
        if (Person.getAllExistingPersons().size() > 0)
            System.out.println("\n2 - add existing");
        int choice = Main.getScan().nextInt();
        Person regPerson;
        if (choice == 1) {
            regPerson = Person.createPerson();
            regPerson.setPersonAsTenant();
            Person.addPersonToExisting(regPerson);

            System.out.print("Enter person rent end: ");
            String rentEnd = Main.getScan().next();
            selPlace.startPlaceRental(regPerson, LocalDate.parse(rentEnd));
        } else if (choice == 2 && Person.getAllExistingPersons().size() > 0) {
            regPerson = Person.findExistingPerson();
            if (regPerson != null) {
                regPerson.setPersonAsTenant();
                selPlace.startPlaceRental(regPerson, LocalDate.parse("20/12/2003"));
            }
        }
    }

    public static void addThing(Parking selParking) throws TooManyThingsException {
        System.out.print("Thing:\n1 - create new ");
        if (Thing.getAllExistingThings().size() > 0)
            System.out.print("\n2 - add existing: ");
        int choice = Main.getScan().nextInt();
        Thing storeThing;
        if (choice == 1) {
            storeThing = Thing.createThing();
            if (storeThing != null) {
                selParking.storedThings.add(storeThing);
                Thing.addThingToExisting(storeThing);
            }
        } else if (choice == 2 && Thing.getAllExistingThings().size() > 0) {
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
        else if (selParking.storedThings.isEmpty()) {
            System.out.println("Nothing stored yet");
        } else {
            System.out.println("------------------");
            System.out.println("Tenant - " + selParking.getTenant().getName());
            System.out.println("20 - Add thing");
            for (int i = 0; i < selParking.storedThings.size(); i++)
                System.out.println(i + " - " + selParking.storedThings.get(i).getName());
        }
    }

    public static Parking selParking(Person selTenant) {
        System.out.println("Select parking:\n");
        for (int i = 0; i < selTenant.getRentedPlaces().size(); i++)
            if (selTenant.getRentedPlaces().get(i) instanceof Parking)
                System.out.println(i + " - " + selTenant.getRentedPlaces().get(i));
        int choice = Main.getScan().nextInt();

        return (Parking) selTenant.getRentedPlaces().get(choice);
    }

    public List<Thing> getStoredThings() {
        return storedThings;
    }

    public int getAvailableSpace() {
        return availableSpace;
    }
}
