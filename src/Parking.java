import java.time.LocalDate;
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
    void rentInitial(Person person, LocalDate rentEnd) {
        if (person.rentedPlacesSize() <= 5) {
            this.tenant = person;

            this.rentStart = DateUpdater.getCurrDate();
            this.rentEnd = rentEnd;
            Place.addToAllRentedPlaces(this);
            person.addRentedPlace(this);

            System.out.println("\nSuccessfully added tenant\n");
            person.selPersonActions(this);
        } else
            System.out.println("Rented places are greater than 5");
    }

    @Override
    public void selectedPlaceDetails() {
        this.showParkingActions();
        String choice = Main.getScan().next();
        this.selParkingAction(choice);
    }

    @Override
    public void showPlaceDetails() {
        this.displayBaseDetails();
        this.displayParkingDetails();
    }

    private void displayParkingDetails() {
        this.getStoredThings();
        System.out.println("Available space: " + this.getAvailableSpace());
    }

    @Override
    protected void clearPlace() {
        this.clearStoredThings();
        this.clearRentStart();
        this.clearRentEnd();
        this.removeStoredThings();
        this.clearArea();
        this.tenant.rentAfterTime = false;
        this.tenant.removeRentedPlace(this);
        this.removeTenant();
    }

    private void clearStoredThings() {
        for (int i = 0; i < this.storedThingsSize(); i++) {
            Thing storedThing = this.storedThings.get(i);
            storedThing.getRidOfThing();
            this.storedThings.remove(storedThing);
        }
    }

    public void selParkingAction(String choice) {
        switch (choice) {
            case "d" -> this.showPlaceDetails();
            case "a" -> this.addParkingTenant();
            case "t" -> this.tenant.selPersonActions(this);
            default -> this.selectedStoredThing(choice);
        }
    }

    protected String showRentedPlaceDetails() {
        return "\n - " + this.getName() + " Parking" + "(" + this.showPlaceContent() + ")"
                + "\n\t-> valid until: " + this.getRentEnd();
    }

    protected void selectedStoredThing(String choice) {
        Thing selThing = this.getStoredThing(Integer.parseInt(choice));
        System.out.println("Selected thing: " + selThing.name);
        selThing.showSpecificThingDetails();
    }

    public Thing showStoredThings() {
        System.out.println("Stored things in " + this.getName() + "\n");
        for (int i = 0; i < this.storedThingsSize(); i++)
            this.getStoredThing(i).displayStoredThing(i);

        int choice2 = Main.getScan().nextInt();
        return this.getStoredThing(choice2);
    }

    private Thing[] getSortedThings() {
        Thing[] sortedThings = new Thing[this.storedThingsSize()];
        if (this.storedThingsSize() > 1)
            for (int i = 0; i < this.storedThingsSize() - 1; i++) {
                for (int j = i + 1; j < this.storedThingsSize(); j++) {
                    if (getStoredThing(i).getArea() < getStoredThing(j).getArea())
                        this.sortThingsBySize(sortedThings, i, j);
                    else if (getStoredThing(i).getArea() == getStoredThing(j).getArea())
                        this.sortThingsByName(sortedThings, i, j);
                    else {
                        sortedThings[i] = getStoredThing(i);
                        sortedThings[j] = getStoredThing(j);
                    }
                }
            }
        else if (this.storedThingsSize() == 1)
            sortedThings[0] = this.getStoredThing(0);
        return sortedThings;
    }

    private void sortThingsBySize(Thing[] sortedThings, int i, int j) {
        sortedThings[i] = getStoredThing(j);
        sortedThings[j] = getStoredThing(i);
    }

    private void sortThingsByName(Thing[] sortedThings, int i, int j) {
        int result = getStoredThing(i).getName().compareTo(getStoredThing(j).getName());
        if (result > 0) {
            sortedThings[i] = getStoredThing(j);
            sortedThings[j] = getStoredThing(i);
        }
    }

    @Override
    public String showPlaceContent() {
        Thing[] sortedThings = getSortedThings();
        String thingsList = "| ";
        if (this.storedThingsSize() > 0)
            for (int i = 0; i < sortedThings.length; i++) {
                if (sortedThings[i] != null)
                    thingsList += sortedThings[i].getName() + " | ";
                else
                    return "nothing inside yet";
            }
        else
            return "nothing inside yet";
        return thingsList.trim();
    }

    public void storeThing(Thing thing) throws TooManyThingsException {
        this.availableSpace = this.decreaseFreeSpace(thing.getArea());
        if (this.availableSpace >= 0)
            this.storedThings.add(thing);
        else
            throw new TooManyThingsException();
    }

    public void addParkingTenant() {
        if (Person.allExistingTenantsSize() > 0) {
            Person.showAllExistingTenants();
            int choice = Main.getScan().nextInt();
            Person regPerson = Person.getExistingPerson(choice);
            this.startRental(regPerson);
        } else
            System.out.println("Not found any tenant");
    }

    public void showParkingActions() {
        System.out.println("\nSelect thing stored in " + this.getName() + ":");
        System.out.println("d - Parking details");

        if (this.getTenant() == null)
            System.out.println("a - Add tenant");
        else {
            System.out.println("t - (Tenant) " + this.tenant.getName());
            System.out.println("------------------");
            this.getStoredThings();

        }
    }

    private void getStoredThings() {
        System.out.print("Stored things: ");
        if (this.storedThings.isEmpty())
            System.out.println("nothing stored yet");
        System.out.println(existingThings());
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
            case 5 -> this.getStoredThings();
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

    private void clearArea() {
        this.availableSpace = this.getVolume();
    }

    private int decreaseFreeSpace(int areaThing) {
        this.availableSpace -= areaThing;
        return this.availableSpace;
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
