import java.time.LocalDate;
import java.util.*;

public class Person {
    public String name;
    public String pesel;
    public String address;
    public String birthday;
    public int id;
    public static int incrementId = 1;
    public boolean isTenant;
    public List<Place> rentedPlaces = new ArrayList<>();
    public List<TenantLetter> letters = new ArrayList<>();
    public static List<Person> allExistingPersons = new ArrayList<>();

    public Person(String firstName, String lastName, String pesel, String birthday, String address) {
        this.pesel = pesel;
        this.birthday = birthday;
        this.id = incrementId++;
        this.name = firstName + " " + lastName;
        this.address = address;
    }

    public void registerPerson(Person person, Place place) {
        if (isTenant && rentedPlaces.contains(place) && rentedPlaces.size() <= 5) {
            if (place.livingPersons.contains(person))
                System.out.println("Person " + person.name + " is already register");
            else {
                place.livingPersons.add(person);
                Person.allExistingPersons.add(person);
                System.out.println("Person " + person.name + "is successfully added");
            }
        } else
            System.out.println("You don't have possibility to register the person");
    }

    public void checkOutPerson(Person person, Apartment place) {
        if (isTenant && rentedPlaces.contains(place)) {
            if (place.livingPersons.contains(person)) {
                place.livingPersons.remove(person);
                Person.allExistingPersons.remove(person);
                System.out.println("Person " + person.name + " is successfully removed");
            } else
                System.out.println("Person " + person.name + " is already register");
        } else
            System.out.println("You don't have possibility to check out the person");
    }

    public void putThing(Thing thing, Parking parking) {
        if (isTenant && rentedPlaces.contains(parking) && rentedPlaces.size() <= 5) {
            if (parking.storedThings.contains(thing))
                System.out.println("Thing " + thing.name + " is already exists");
            else {
                parking.storedThings.add(thing);
                System.out.println("Thing " + thing.name + " is successfully added");
            }
        } else
            System.out.println("You don't have possibility to put the thing");
    }

    public void removeThing(Thing thing, Parking parking) {
        if (isTenant && rentedPlaces.contains(parking)) {
            if (parking.storedThings.contains(thing)) {
                parking.storedThings.remove(thing);
                System.out.println("Thing " + thing.name + " is successfully removed");
            } else
                System.out.println("Thing " + thing.name + " is not exists");
        } else
            System.out.println("You don't have possibility to put the thing");
    }

    public void renewRental(Place place, String rentEnd) {
        if (this.isTenant && this.rentedPlaces.contains(place)) {
            for (TenantLetter tenantLetter : place.tenant.letters) {
                if (tenantLetter.place == place && tenantLetter.tenantName.equals(place.tenant.name)) {
                    place.rentEnd = LocalDate.parse(rentEnd);
                    place.rentStart = Main.currDate;
                    place.tenant.letters.remove(tenantLetter);
                }
            }
        } else if (this.isTenant) {
            place.rentStart = Main.currDate;
            place.rentEnd = LocalDate.parse(rentEnd);
        }
    }

    public void cancelRental(Place place) {
        if (this.isTenant && this.rentedPlaces.contains(place)) {
            for (TenantLetter tenantLetter : place.tenant.letters)
                if (tenantLetter.place == place && tenantLetter.tenantName.equals(place.tenant.name))
                    place.tenant.letters.remove(tenantLetter);
//            place.rentStart = null;
            place.rentEnd = Main.currDate;
        }
    }

/*
    public void checkRentedPlacesValidity() throws ParseException {
        for (int place = 0; place < this.rentedPlaces.length; place++) {
            int compareDates = this.rentedPlaces[place].rentEnd.compareTo(Main.currDate);
            if (compareDates > 0)
                this.rentedPlaceAfterTime(place);
        }
    }

    private void rentedPlaceAfterTime(int place) throws ParseException {
        Person tenant = this.rentedPlaces[place].tenant;
        Arrays.asList(tenant.letters).add(
            new TenantLetter(tenant.name, this.rentedPlaces[place]
        ));
    }
*/
}
