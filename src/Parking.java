import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public void selectedPlaceDetails() {
        this.showStoredThings();
        String choice = Main.getScan().next();
        Thing.selThingOption(choice, this);
    }

    @Override
    public void showPlaceDetails() {
        this.displayBaseDetails();
        this.displayParkingDetails();
    }

    private void displayParkingDetails() {
        this.displayStoredThings();
        System.out.println("Available space: " + this.getAvailableSpace());
    }

    @Override
    protected void clearPlace() {
        this.clearStoredThings();
        this.rentEnd = null;
        this.rentStart = null;
        this.removeStoredThings();
        this.availableSpace = this.currFreeSpace();
        this.clearRentEnd();
        this.tenant.rentAfterTime = false;
        this.tenant.removeRentedPlace(this);
        this.removeTenant();
    }

    private void clearStoredThings() {
        for (int i = 0; i < this.storedThingsSize(); i++) {
            Thing storedThing = this.storedThings.get(i);
            Thing.addToUtilization(storedThing);
            this.storedThings.remove(storedThing);
        }
    }

    private Thing[] getSortedThings() {
        Thing[] sortedThings = new Thing[this.storedThingsSize()];
        for (int i = 0; i < this.storedThingsSize(); i++) {
            if (this.storedThingsSize() > 1) {
                for (int j = i; j < this.storedThingsSize() - 1; j++) {
                    if (getStoredThing(j).getArea() < getStoredThing(j + 1).getArea())
                        this.sortThingsBySize(sortedThings, j);
                    else if (getStoredThing(j).getArea() == getStoredThing(j + 1).getArea())
                        this.sortThingsByName(sortedThings, j);
                }
            } else
                sortedThings[0] = getStoredThing(0);
        }
        return sortedThings;
    }

    private void sortThingsBySize(Thing[] sortedThings, int j) {
        sortedThings[j] = getStoredThing(j + 1);
        sortedThings[j + 1] = getStoredThing(j);
    }

    private void sortThingsByName(Thing[] sortedThings, int j) {
        int result = getStoredThing(j).getName().compareTo(getStoredThing(j + 1).getName());
        if (result < 0) {
            sortedThings[j] = getStoredThing(j);
            sortedThings[j + 1] = getStoredThing(j + 1);
        } else if (result > 0) {
            sortedThings[j] = getStoredThing(j + 1);
            sortedThings[j + 1] = getStoredThing(j);
        } else {
            sortedThings[j] = getStoredThing(j);
            sortedThings[j + 1] = getStoredThing(j + 1);
        }
    }

    @Override
    public String showPlaceContent() {
        Thing[] sortedThings = getSortedThings();
        String thingsList = "| ";
        if (this.livingPersonsSize() > 0)
            for (int i = 0; i < sortedThings.length; i++)
                thingsList += sortedThings[i].getName() + " | ";
        else
            return "nothing inside yet";
        return thingsList.trim();
    }

    public void storeThing(Thing thing) throws TooManyThingsException {
        this.availableSpace = this.currFreeSpace();
        if (thing.getArea() <= this.availableSpace)
            this.storedThings.add(thing);
        else
            throw new TooManyThingsException();
    }

    private int currFreeSpace() {
        if (this.storedThingsSize() > 0)
            for (Thing storeThing : this.storedThings)
                this.availableSpace -= storeThing.getArea();
        else
            this.availableSpace = this.getVolume();
        return this.availableSpace;
    }

    public void addParkingTenant() {
        if (Person.allExistingTenantsSize() > 0) {
            Person.showAllExistingTenants();
            int choice = Main.getScan().nextInt();
            Person regPerson = Person.getExistingPerson(choice);
            this.rentalConfig(regPerson);
        } else
            System.out.println("Not found any tenant");
    }

    public static void addThing(Parking selParking) throws TooManyThingsException {
        System.out.print("Thing:\n1 - create new ");
        int choice = Main.getScan().nextInt();
        Thing storeThing;

        if (choice == 1) {
            storeThing = Thing.createThing();
            if (storeThing != null) {
                selParking.storedThings.add(storeThing);
                Thing.addThingToExisting(storeThing);
                System.out.println("Successfully added new thing");
            }
        }
    }

    public void showStoredThings() {
        System.out.println("\nSelect thing stored in " + this.getName() + ":");
        System.out.println("15 - Parking details");

        if (this.getTenant() == null)
            System.out.println("9 - Add tenant");
        else {
            System.out.println("t - (Tenant) " + this.getTenant().getName());
            System.out.println("------------------");
            System.out.println("Stored things: " + this.getStoredThings());

        }
    }

    private String getStoredThings() {
        if (this.storedThings.isEmpty())
            return "nothing stored yet";
        return existingThings();
    }

    private String existingThings() {
        String storedThingsList = "";
        for (int i = 0; i < this.storedThingsSize(); i++)
            storedThingsList += "\n" + i + " - " + this.storedThings.get(i).getName();
        return storedThingsList;
    }

    @Override
    public void placeActions(Person selTenant, int choice) {
        switch (choice) {
            case 1 -> selTenant.putThing(this);
            case 2 -> selTenant.removeThing(this);
            case 3 -> selTenant.cancelRental(this);
            case 4 -> selTenant.renewRental(this);
            case 5 -> this.displayStoredThings();
            case 6 -> selTenant.showPersonDetails();
            default -> System.out.println("Entered digit does not have an action");
        }
    }

    @Override
    public void showPlaceActions() {
        System.out.println("1 - put thing");
        System.out.println("2 - remove thing");
        System.out.println("3 - cancel rental");
        System.out.println("4 - renew rental");
        System.out.println("5 - show stored things");
        System.out.println("6 - about the person");
    }

    public static void readyParkings(Block block) {
        Parking parking = new Parking("parking1", 30);
        Parking parking2 = new Parking("parking2", 54);
        Parking parking3 = new Parking("parking3", 24);
        Parking parking4 = new Parking("parking4", 32);
        block.assignPlace(Arrays.asList(parking, parking2, parking3, parking4));
    }

    public Parking chosenParking(Person selTenant) {
        if (selTenant.rentedPlacesSize() > 0) {
            System.out.println("Select parking:");
            for (int i = 0; i < selTenant.rentedPlacesSize(); i++)
                if (selTenant.getRentedPlace(i) instanceof Parking)
                    System.out.println(i + " - " + selTenant.getRentedPlace(i).getName());
            int choice = Main.getScan().nextInt();

            return (Parking) selTenant.getRentedPlace(choice);
        }
        return null;
    }

    public void displayStoredThings() {
        System.out.print("Stored things:");
        if (this.storedThings.size() > 0)
            for (int i = 0; i < this.storedThings.size(); i++)
                System.out.println("\n- " + this.storedThings.get(i).getName());
        else
            System.out.println(" nothing stored yet");
    }

    private void removeStoredThings() {
        this.storedThings.clear();
    }

    public Thing getStoredThing(int index) {
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
