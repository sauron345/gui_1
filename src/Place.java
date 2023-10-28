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
            throw new ProblematicTenantException(person.name, person.rentedPlaces);

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

}
