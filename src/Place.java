import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

abstract public class Place {
    private int id;
    private static int incrementId = 1;
    private String name;
    private int volume;
    private Person tenant = null;
    private LocalDate rentStart, rentEnd;
    private List<Person> livingPersons = new ArrayList<>();
    private static List<Place> allRentedPlaces = new ArrayList<>();
    private static List<Place> allExistingPlaces = new ArrayList<>();

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

        if (person.isTenant() && person.lettersSize() >= 3)
            throw new ProblematicTenantException(person.getName());

        if (person.isTenant() && !person.checkRentedPlaceExists(this) && person.rentedPlacesSize() <= 5) {
            this.tenant = person;

            if (this.tenant.lettersSize() > 0)
                person.clearLetters();

            this.rentStart = Main.getCurrDate();
            this.rentEnd = rentEnd;
            Place.addToAllRentedPlaces(this);
            person.addRentedPlace(this);
            this.livingPersons.add(person);

            if (this.livingPersons.size() == 1 && this instanceof Apartment)
                ((Apartment) this).setPersonPayRent(person);
        }
    }

    protected void tenantConfig(Person regPerson, Place selPlace) {
        if (selPlace instanceof  Apartment)
            ((Apartment) selPlace).addPersonPayRent(regPerson);
        if (!regPerson.isTenant())
            regPerson.setPersonAsTenant();
        selPlace.startRental(regPerson);
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

    public static void showPlaceDetails(Place selPlace) {
        System.out.println("Name: " + selPlace.name);
        System.out.println("Tenant: " + selPlace.checkTenantExists());
        System.out.println("Volume: " + selPlace.volume);
        System.out.println("Rented start: " + selPlace.checkRentStart());
        System.out.println("Rented end: " + selPlace.checkRentEnd());

        if (selPlace instanceof Apartment) {
            System.out.println("Person pay rent: " + ((Apartment) selPlace).checkPersonPayRent());
            if (selPlace.livingPersons.isEmpty())
                System.out.println("Living persons: none");
            else
                selPlace.displayLivingPersons(selPlace);
        } else if (selPlace instanceof Parking) {
            ((Parking) selPlace).displayStoredThings();
            System.out.println("Available space: " + ((Parking) selPlace).getAvailableSpace());
        }
    }

    private void displayLivingPersons(Place selPlace) {
        System.out.println("Living persons:");
        for (int i = 0; i < selPlace.livingPersons.size(); i++) {
            System.out.println("- " + selPlace.livingPersons.get(i));
        }
    }

    public void endPlaceRental() {
        if (this.tenant.lettersSize() > 0)
            this.tenant.clearLetters();
        this.rentEnd = Main.getCurrDate();
    }

    private void clearPlace() {
        if (this instanceof Apartment)
            this.clearApartment();
        else if (this instanceof Parking)
            ((Parking) this).clearParking();
    }

    private void clearApartment() {
        this.livingPersons = null;
        this.tenant = null;
        ((Apartment) this).setPersonPayRent(null);
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
            return Arrays.asList(name, volume.get(0), volume.get(1), volume.get(2), type);
        else
            return Arrays.asList(name, volume.get(0), type);
    }

    private static Place createPlace() {
        List<String> placeData = Place.enterPlaceData();

        if (placeData.get(2).equals("Apartment") || placeData.get(4).equals("Apartment")) {
            if (placeData.size() > 3)
                return new Apartment(placeData.get(0), Integer.parseInt(placeData.get(1)), Integer.parseInt(placeData.get(2)), Integer.parseInt(placeData.get(1)));
            else
                return new Apartment(placeData.get(0), Integer.parseInt(placeData.get(1)));
        } else if (placeData.get(2).equals("Parking") || placeData.get(4).equals("Parking")) {
            if (placeData.size() == 4)
                return new Parking(placeData.get(0), Integer.parseInt(placeData.get(1)),
                        Integer.parseInt(placeData.get(2)), Integer.parseInt(placeData.get(3)));
            else
                return new Parking(placeData.get(0), Integer.parseInt(placeData.get(1)));
        }
        return null;
    }

    public static void checkRentedPlacesValidity() throws ParseException {
        for (int i = 0; i < Place.allRentedPlacesSize(); i++) {
            Person tenant = Place.getRentedPlace(i).tenant;
            if (Place.getRentedPlace(i).rentEnd != null) {
                if (Place.getRentedPlace(i).rentEnd.isAfter(Main.getCurrDate()))
                    Place.rentedPlaceAfterTime(i, tenant);
                for (int j = 0; j < tenant.lettersSize(); j++)
                    if (tenant.getLetter(j).getReceivedDate().compareTo(Main.getCurrDate()) > 30)
                        tenant.getLetter(j).getPlace().clearPlace();
            }
        }
    }

    public static void showPlaceOptions(Estate selEstate) {
        System.out.println("\nSelect place of " + selEstate.getName() + ":");
        System.out.println("20 - Add new place");

        if (selEstate.placesSize() > 0) {
            System.out.println("21 - remove place");
            System.out.println("------------------");
            System.out.println("Existing places:");
            for (int i = 0; i < selEstate.placesSize(); i++)
                System.out.println(i + " - " + selEstate.getPlace(i).name);
        } else
            System.out.println("No places available yet");
    }

    public static Place selPlaceOption(int choice, Estate selEstate) {
        Place selPlace;
        if (choice == 20) {
            Place placeCreated = Place.createPlace();
            return selEstate.addPlace(placeCreated);
        } else if (choice == 21 && Estate.allExistingEstatesSize() > 0) {
            selPlace = Place.findExistingPlace();
            if (selPlace != null)
                selEstate.removePlace(selPlace);
            return selPlace;
        } else
            return selEstate.getPlace(choice);
    }

    private static void rentedPlaceAfterTime(int i, Person tenant) {
        TenantLetter tenantLetter = new TenantLetter(tenant.getName(), Place.allRentedPlaces.get(i));
        tenant.addLetter(tenantLetter);
        Place.allRentedPlaces.get(i).rentEnd = null;
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

    private String checkTenantExists() {
        if (this.tenant == null)
            return "none";
        else
            return String.valueOf(this.tenant);
    }

    public int livingPersonsSize() {
        return this.livingPersons.size();
    }

    public boolean checkLivingPersonExists(Person person) {
        return this.livingPersons.contains(person);
    }

    public Person getLivingPerson(int index) {
        return this.livingPersons.get(index);
    }

    public boolean checkLivingPersonsIsEmpty() {
        return this.livingPersons.isEmpty();
    }

    private static Place getRentedPlace(int index) {
        return Place.allRentedPlaces.get(index);
    }

    public static int allRentedPlacesSize() {
        return Place.allRentedPlaces.size();
    }

    public static void addPlaceToExisting(Place place) {
        Place.allExistingPlaces.add(place);
    }

    public static void addPlaceToExisting(List<Place> places) {
        Place.allExistingPlaces.addAll(places);
    }

    public static void addToAllRentedPlaces(Place place) {
        Place.allRentedPlaces.add(place);
    }

    public void addPersonToPlace(Person person) {
        this.livingPersons.add(person);
    }

    public void removePersonToPlace(Person person) {
        this.livingPersons.remove(person);
    }

    public void setRentEnd(LocalDate rentEnd) {
        this.rentEnd = rentEnd;
    }

    public void setRentStart() {
        this.rentStart = Main.getCurrDate();
    }


    public int getId() {
        return id;
    }

    public static int getIncrementId() {
        return incrementId;
    }

    public String getName() {
        return name;
    }

    public int getVolume() {
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
