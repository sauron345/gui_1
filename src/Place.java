import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.time.temporal.ChronoUnit;

abstract public class Place {
    private final int id;
    private static int incrementId = 1;
    private final String name;
    protected int volume;
    protected Person tenant = null;
    protected LocalDate rentStart, rentEnd;
    protected List<Person> livingOrdinaryPersons = new ArrayList<>();
    private static List<Place> allRentedPlaces = new ArrayList<>();
    private static List<Place> allExistingPlaces = new ArrayList<>();
    public boolean rentAfterTime = false;
    private TenantLetter tenantLetter;

    public Place(String name, int volume) {
        this.id = incrementId++;
        this.volume = volume;
        this.name = name;
    }

    public Place(String name, int height, int width, int length) {
        this.id = incrementId++;
        this.volume = height * width * length;
        this.name = name;
    }

    private void startRental(Person person) {
        LocalDate rentEnd = person.enterPersonRentEnd();

        if (person.isTenant() && person.lettersCount() >= 3)
            throw new ProblematicTenantException(person.getName());

        if (person.isTenant() && !person.checkRentedPlaceExists(this) && person.rentedPlacesSize() <= 5)
            this.rentInitial(person, rentEnd);
    }

    public void rentedPlaceAfterTime(int i) {
        if (this.rentAfterTime)
            this.checkExceedLetterDate();
        else {
            this.tenantLetter = new TenantLetter(this.tenant.getName(), Place.getRentedPlace(i));
            this.tenant.addLetter(this.tenantLetter);
            this.rentAfterTime = true;
        }
    }

    private void checkExceedLetterDate() {
        int compareDifferentDates = (int) ChronoUnit.DAYS.between(this.tenantLetter.getReceivedDate(), DateUpdater.getCurrDate());
        if (compareDifferentDates > 30) {
            this.clearPlace();
            System.out.println("Time has passed, tenant: " + this.tenantLetter.getPersonName() +
                ", has been removed from " + this.tenantLetter.getPlace().getName());
            this.removeTenantLetter();
        }
    }

    private void rentInitial(Person person, LocalDate rentEnd) {
        this.tenant = person;

        this.rentStart = DateUpdater.getCurrDate();
        this.rentEnd = rentEnd;
        Place.addToAllRentedPlaces(this);
        person.addRentedPlace(this);

        if (this instanceof Apartment)
            ((Apartment) this).setPersonPayRent(person);

        System.out.println("\nSuccessfully added tenant\n");
        person.selPersonActions(this);
    }

    protected void rentalConfig(Person regPerson) {
        if (this instanceof  Apartment)
            ((Apartment) this).setPersonPayRent(regPerson);
        if (!regPerson.isTenant())
            regPerson.setPersonAsTenant();
        this.startRental(regPerson);
    }

    private static List<String> enterExistingPlaceData() {
        System.out.print("Enter place name: ");
        String name = Main.getScan().next();

        System.out.print("Enter place volume: ");
        String volume = Main.getScan().next();

        return Arrays.asList(name, volume);
    }

    private static Place findExistingPlace() {
        List<String> estateData = Place.enterExistingPlaceData();
        int same = 0;
        for (Place place : Place.allExistingPlaces) {
            if (place.name.equals(estateData.get(0)))
                same++;
            else if (place.name.equals(estateData.get(1)))
                same++;
            if (same == 2)
                return place;
        }
        System.out.println("Entered place is not exists");
        return null;
    }

    protected void displayBaseDetails() {
        System.out.println("Name: " + this.name);
        System.out.println("Tenant: " + this.getTenantIfExists());
        System.out.println("Volume: " + this.volume);
        System.out.println("Rented start: " + this.checkRentStart());
        System.out.println("Rented end: " + this.checkRentEnd());
    }

    private static List<String> enterPlaceData() {
        System.out.print("Enter place name: ");
        String name = Main.getScan().next();

        System.out.println("Enter:\n1 - volume\n2 - width, height, length");
        int choice = Main.getScan().nextInt();
        List<String> volume = Main.enterVolumeData(choice);

        System.out.print("Enter type place (Parking or Apartment): ");
        String type = Main.getScan().next();

        if (volume.size() > 1)
            return Arrays.asList(type, name, volume.get(0), volume.get(1), volume.get(2));
        else
            return Arrays.asList(type, name, volume.get(0), "", "");
    }

    private static Place createPlace() {
        List<String> placeData = Place.enterPlaceData();

        if (placeData.get(0).equals("Apartment") && placeData.get(3).equals("") && placeData.get(4).equals(""))
            return new Apartment(placeData.get(1), Integer.parseInt(placeData.get(2)));
        else if (placeData.get(0).equals("Apartment"))
            return new Apartment(placeData.get(1), Integer.parseInt(placeData.get(2)), Integer.parseInt(placeData.get(3)), Integer.parseInt(placeData.get(4)));
        else if (placeData.get(0).equals("Parking") && placeData.get(3).equals("") && placeData.get(4).equals(""))
            return new Parking(placeData.get(1), Integer.parseInt(placeData.get(2)));
        else if (placeData.get(0).equals("Parking"))
            return new Parking(placeData.get(1), Integer.parseInt(placeData.get(2)),
                    Integer.parseInt(placeData.get(3)), Integer.parseInt(placeData.get(4)));

        return null;
    }

    public static void checkRentedPlacesValidity() throws ParseException {
        for (int i = 0; i < Place.allRentedPlacesSize(); i++) {
            Place rentedPlace = Place.getRentedPlace(i);
            LocalDate rentStart = rentedPlace.getRentStart();
            LocalDate rentEnd = rentedPlace.getRentEnd();

            if (rentStart != null && rentEnd != null)
                rentedPlace.checkRentedPlace(i);
        }
    }

    private void checkRentedPlace(int i) {
        boolean RentAfterTime = DateUpdater.getCurrDate().isAfter(this.rentEnd);
        if (RentAfterTime)
            this.rentedPlaceAfterTime(i);
    }

    public static void showPlaceOptions(Block selBlock) {
        System.out.println("\nSelect place of " + selBlock.getName() + ":");
        System.out.println("a - Add new place");

        if (selBlock.placesSize() > 0) {
            System.out.println("r - remove place");
            System.out.println("------------------");
            System.out.println("Existing places:");
            for (int i = 0; i < selBlock.placesSize(); i++)
                System.out.println(i + " - " + selBlock.getPlace(i).name);
        } else
            System.out.println("No places available yet");
    }

    public static Place selPlaceOption(String choice, Block selBlock) {
        Place selPlace;
        if (choice.equals("a")) {
            Place placeCreated = Place.createPlace();
            return selBlock.assignPlace(placeCreated);
        } else if (choice.equals("r") && Block.allExistingBlocksSize() > 0) {
            selPlace = Place.findExistingPlace();
            if (selPlace != null)
                selBlock.removePlace(selPlace);
            return selPlace;
        } else
            return selBlock.getPlace(Integer.parseInt(choice));
    }

    private String checkRentEnd() {
        if (this.rentEnd == null)
            return "not yet";
        else
            return String.valueOf(this.rentEnd);
    }

    private String checkRentStart() {
        if (this.rentStart == null)
            return "not yet";
        else
            return String.valueOf(this.rentStart);
    }

    private String getTenantIfExists() {
        if (this.tenant == null)
            return "none";
        else
            return this.tenant.getName();
    }

    abstract public void selectedPlaceDetails();

    abstract public void showPlaceDetails();

    abstract void placeActions(Person selTenant, int choice);

    abstract void showPlaceActions();

    abstract protected void clearPlace();

    public abstract String showPlaceContent();

    protected void removeTenant() {
        this.tenant = null;
    }

    public int livingPersonsSize() {
        return this.livingOrdinaryPersons.size();
    }

    protected void clearRentEnd() {
        this.rentEnd = null;
    }

    public void clearLivingPersons() {
        this.livingOrdinaryPersons.clear();
    }

    public boolean checkLivingPersonExists(Person person) {
        return this.livingOrdinaryPersons.contains(person);
    }

    public Person getLivingPerson(int index) {
        return this.livingOrdinaryPersons.get(index);
    }

    public boolean checkLivingPersonsIsEmpty() {
        return this.livingOrdinaryPersons.size() == 0;
    }

    public static Place getRentedPlace(int index) {
        return Place.allRentedPlaces.get(index);
    }

    public static int allRentedPlacesSize() {
        return Place.allRentedPlaces.size();
    }

    public static void addToAllRentedPlaces(Place place) {
        Place.allRentedPlaces.add(place);
    }

    public static void addToAllExistingPlaces(Place place) {
        Place.allExistingPlaces.add(place);
    }

    public static void addToAllExistingPlaces(List<Place> places) {
        Place.allExistingPlaces.addAll(places);
    }

    public void addPersonToPlace(Person person) {
        this.livingOrdinaryPersons.add(person);
    }

    public void removePersonToPlace(Person person) {
        this.livingOrdinaryPersons.remove(person);
    }

    public void setRentEnd(LocalDate rentEnd) {
        this.rentEnd = rentEnd;
    }

    public void setRentStart() {
        this.rentStart = DateUpdater.getCurrDate();
    }

    public int getId() {
        return id;
    }

    private void removeTenantLetter() {
        this.tenantLetter = null;
    }

    public static int getIncrementId() {
        return incrementId;
    }

    public String getName() {
        return name;
    }

    protected int getVolume() {
        return volume;
    }

    public Person getTenant() {
        return tenant;
    }

    public LocalDate getRentStart() {
        return rentStart;
    }

    public LocalDate getRentEnd() {
        return rentEnd;
    }

}
