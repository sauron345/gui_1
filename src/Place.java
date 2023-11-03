import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

abstract public class Place {
    private int id;
    static int incrementId = 1;
    private String name;
    private int volume;
    private Person tenant;
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

    public static void addPlaceToExisting(Place place) {
        Place.allExistingPlaces.add(place);
    }

    public static void addPlaceToExisting(List<Place> places) {
        Place.allExistingPlaces.addAll(places);
    }

    public static void addPlaceToRented(Place place) {
        Place.allRentedPlaces.add(place);
    }

    public void addPersonToPlace(Person person) {
        this.livingPersons.add(person);
    }

    public void removePersonToPlace(Person person) {
        this.livingPersons.remove(person);
    }

    public void setRentEnd(String rentEnd) {
        this.rentEnd = LocalDate.parse(rentEnd);
    }

    public void setRentStart() {
        this.rentStart = Main.getCurrDate();
    }



    public void startPlaceRental(Person person, LocalDate endRental) throws ProblematicTenantException {
        if (person.isTenant() && person.getLetters().size() >= 3)
            throw new ProblematicTenantException(person.getName());

        if (person.isTenant() && !person.getRentedPlaces().contains(this) && person.getRentedPlaces().size() <= 5) {
            this.tenant = person;

            if (this.tenant.getLetters().size() > 0)
                person.clearLetters();

            this.rentStart = Main.getCurrDate();
            this.rentEnd = endRental;
            Place.addPlaceToRented(this);
            this.livingPersons.add(person);

            if (this.livingPersons.size() == 1 && this instanceof Apartment)
                ((Apartment) this).setPersonPayRent(person);
        }
    }


    public static List<String> enterExistingPlaceData() {
        System.out.print("Enter place name: ");
        String name = Main.getScan().next();

        System.out.print("Enter place volume: ");
        String volume = Main.getScan().next();

        return Arrays.asList(name, volume);
    }

    public static Place findExistingPlace() {
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
        System.out.println("Tenant: " + selPlace.tenant);
        System.out.println("Volume: " + selPlace.volume);
        System.out.println("Rented start: " + selPlace.rentStart);
        System.out.println("Rented end: " + selPlace.rentEnd);

        if (selPlace instanceof Apartment) {
            System.out.println("Person pay rent: " + ((Apartment) selPlace).getPersonPayRent());
            if (selPlace.livingPersons.isEmpty())
                System.out.println("Living persons: none");
            else
                System.out.println("Living persons: " + selPlace.livingPersons.toString());
        } else if (selPlace instanceof Parking) {
            System.out.println("Stored things: " + ((Parking) selPlace).getStoredThings().toString());
            System.out.println("Available space: " + ((Parking) selPlace).getAvailableSpace());
        }
    }

    public void endPlaceRental() {
        if (this.tenant.getLetters().size() > 0)
            this.tenant.clearLetters();
        this.rentEnd = Main.getCurrDate();
    }

    public void clearPlace() {
        if (this instanceof Apartment)
            this.clearApartment();
        else if (this instanceof Parking)
            ((Parking) this).clearParking();
    }

    public void clearApartment() {
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

    public static Place createPlace() {
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
        for (int i = 0; i < Place.allRentedPlaces.size(); i++) {
            Person tenant = Place.allRentedPlaces.get(i).tenant;
            if (Place.allRentedPlaces.get(i).rentEnd.isAfter(Main.getCurrDate()))
                Place.rentedPlaceAfterTime(i, tenant);
            for (int j = 0; j < tenant.getLetters().size(); j++) {
                if (tenant.getLetters().get(j).getReceivedDate().compareTo(Main.getCurrDate()) > 30) {
                    tenant.getLetters().get(j).getPlace().clearPlace();
                }
            }
        }
    }

    public static void showPlaceOptions(Estate selEstate) {
        System.out.println("\nSelect place of " + selEstate.getName() + ":");
        System.out.println("20 - Add new place");

        if (selEstate.getPlaces().size() > 0) {
            System.out.println("21 - remove place");
            System.out.println("------------------");
            System.out.println("Existing places:");
            for (int i = 0; i < selEstate.getPlaces().size(); i++)
                System.out.println(i + " - " + selEstate.getPlaces().get(i).name);
        } else
            System.out.println("No places available yet");
    }

    public static Place selPlaceOption(int choice, Estate selEstate) {
        Place selPlace;
        if (choice == 20) {
            Place placeCreated = Place.createPlace();
            return selEstate.addPlace(placeCreated);
        } else if (choice == 21 && Estate.getAllExistingEstates().size() > 0) {
            selPlace = Place.findExistingPlace();
            if (selPlace != null)
                selEstate.removePlace(selPlace);
            return selPlace;
        } else
            return selEstate.getPlaces().get(choice);
    }

    private static void rentedPlaceAfterTime(int i, Person tenant) {
        TenantLetter tenantLetter = new TenantLetter(tenant.getName(), Place.allRentedPlaces.get(i));
        tenant.addLetter(tenantLetter);
        Place.allRentedPlaces.get(i).rentEnd = null;
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

    public List<Person> getLivingPersons() {
        return livingPersons;
    }

    public static List<Place> getAllRentedPlaces() {
        return allRentedPlaces;
    }

    public static List<Place> getAllExistingPlaces() {
        return allExistingPlaces;
    }

}
