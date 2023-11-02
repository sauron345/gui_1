import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

abstract public class Place {
    private int id;
    static int incrementId = 1;
    public String name;
    public int volume;
    public Person tenant;
    LocalDate rentStart, rentEnd;
    public List<Person> livingPersons = new ArrayList<>();
    public static List<Place> allRentedPlaces = new ArrayList<>();
    public static List<Place> allExistingPlaces = new ArrayList<>();

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

    public void startPlaceRental(Person person, LocalDate endRental) throws ProblematicTenantException {
        if (person.isTenant && person.letters.size() >= 3)
            throw new ProblematicTenantException(person.name);

        if (person.isTenant && !person.rentedPlaces.contains(this) && person.rentedPlaces.size() <= 5) {
            this.tenant = person;

            if (this.tenant.letters.size() > 0)
                person.letters = null;

            this.rentStart = Main.currDate;
            this.rentEnd = endRental;
            person.rentedPlaces.add(this);
            this.livingPersons.add(person);

            if (this.livingPersons.size() == 1 && this instanceof Apartment)
                ((Apartment) this).personPayRent = person;
        }
    }

    public static List<String> enterPlaceName() {
        System.out.print("Enter place name: ");
        String name = Main.scan.next();

        System.out.print("Enter place volume: ");
        String volume = Main.scan.next();

        return Arrays.asList(name, volume);
    }

    public static Place findExistingPlace() {
        List<String> estateData = Place.enterPlaceName();
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
            System.out.println("Person pay rent: " + ((Apartment) selPlace).personPayRent);
            if (selPlace.livingPersons.isEmpty())
                System.out.println("Living persons: none");
            else
                System.out.println("Living persons: " + selPlace.livingPersons.toString());
        } else if (selPlace instanceof Parking) {
            System.out.println("Stored things: " + ((Parking) selPlace).storedThings.toString());
            System.out.println("Available space: " + ((Parking) selPlace).availableSpace);
        }
    }

    public void endPlaceRental() {
        if (this.tenant.letters.size() > 0)
            this.tenant.letters = null;
        this.rentEnd = Main.currDate;
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
        ((Apartment) this).personPayRent = null;
    }

    private static List<String> enterPlaceData() {
        System.out.print("Enter place name: ");
        String name = Main.scan.next();

        System.out.println("Enter:\n1 - volume\n2 - width, height, length");
        int choice = Main.scan.nextInt();
        List<String> volume = Main.enterVolumeData(choice);

        System.out.print("Enter type place (Parking or Apartment): ");
        String type = Main.scan.next();

        if (volume.size() > 1)
            return Arrays.asList(name, volume.get(0), volume.get(1), volume.get(2), type);
        else
            return Arrays.asList(name, volume.get(0), type);
    }

    public static Place createPlace() {
        List<String> placeData = Place.enterPlaceData();

        if (placeData.get(2).equals("Apartment")) {
            if (placeData.size() > 3)
                return new Apartment(placeData.get(0), Integer.parseInt(placeData.get(1)), Integer.parseInt(placeData.get(2)), Integer.parseInt(placeData.get(1)));
            else
                return new Apartment(placeData.get(0), Integer.parseInt(placeData.get(1)));
        } else if (placeData.get(2).equals("Parking")) {
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
            if (Place.allRentedPlaces.get(i).rentEnd.isAfter(Main.currDate))
                Place.rentedPlaceAfterTime(i, tenant);
            for (int j = 0; j < tenant.letters.size(); j++) {
                if (tenant.letters.get(j).receivedDate.compareTo(Main.currDate) > 30) {
                    tenant.letters.get(j).place.clearPlace();
                }
            }
        }
    }

    public static void showPlaceOptions(Estate selEstate) {
        System.out.println("\nSelect place of " + selEstate.name + ":");
        System.out.println("20 - Add new place");

        if (selEstate.places.size() > 0) {
            System.out.println("21 - remove place");
            System.out.println("------------------");
            System.out.println("Existing places:");
            for (int i = 0; i < selEstate.places.size(); i++)
                System.out.println(i + " - " + selEstate.places.get(i).name);
        } else
            System.out.println("No places available yet");
    }

    public static Place selPlaceOption(int choice, Estate selEstate) {
        Place selPlace;
        if (choice == 20) {
            Place placeCreated = Place.createPlace();
            return selEstate.addPlace(placeCreated);
        } else if (choice == 21 && Estate.allExistingEstates.size() > 0) {
            selPlace = Place.findExistingPlace();
            if (selPlace != null)
                selEstate.removePlace(selPlace);
            return selPlace;
        } else
            return selEstate.places.get(choice);
    }

    private static void rentedPlaceAfterTime(int i, Person tenant) {
        tenant.letters.add(
            new TenantLetter(tenant.name, Place.allRentedPlaces.get(i)
        ));
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
