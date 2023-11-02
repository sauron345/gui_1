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

    private void registerPerson(Person person, Place place) {
        if (isTenant) {
            if (rentedPlaces.contains(place) && rentedPlaces.size() <= 5) {
                if (place.livingPersons.contains(person))
                    System.out.println("Person " + person.name + " is already register");
                else {
                    place.livingPersons.add(person);
                    Person.allExistingPersons.add(person);
                    System.out.println("Person " + person.name + "is successfully added");
                }
            } else
                System.out.println("Place is not exists or rented places are greater than 5");
        } else
            System.out.println("You don't have possibility to register the person");
    }

    private void checkOutPerson(Person person, Apartment place) {
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

    public static void showPersonsLiving(Place selPlace) {
        System.out.println("\nSelect person living in " + selPlace.name + " :");
        System.out.println("15 - Apartment details");
        if (selPlace.livingPersons.isEmpty()) {
            System.out.println("No one");
            System.out.println("9 - Add tenant");
        } else {
            System.out.println("------------------");
            System.out.println("Living persons:");
            for (int i = 0; i < selPlace.livingPersons.size(); i++) {
                if (selPlace.livingPersons.get(i).isTenant)
                    System.out.println(i + " - (Tenant) " + selPlace.livingPersons.get(i).name);
                else
                    System.out.println(i + " - " + selPlace.livingPersons.get(i).name);
            }
        }

    }

    public static Person selPersonLiving(int choice, Place selPlace) {
        Person selPerson;
        if (choice == 15) {
            Place.showPlaceDetails(selPlace);
        } else if (selPlace.livingPersons.isEmpty() && choice == 9) {
            Apartment.addApartTenant((Apartment) selPlace);
        } else {
            selPerson = selPlace.livingPersons.get(choice);
            System.out.println("Selected person: " + selPerson.name);
            Person.selPersonActions(selPerson, selPlace);
        }
        return null;
    }


    public void removeThing(Thing thing, Parking parking) {
        if (isTenant && rentedPlaces.contains(parking)) {
            if (parking.storedThings.contains(thing)) {
                parking.storedThings.remove(thing);
                System.out.println("Thing " + thing.getName() + " is successfully removed");
            } else
                System.out.println("Thing " + thing.getName() + " is not exists");
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

    public static void selPersonActions(Person selPerson, Place selPlace) {
        int choice;
        if (selPerson.isTenant) {
            Person.showTenantActions();
            choice = Main.scan.nextInt();
            Person.tenantActions(selPerson, selPlace, choice);
        } else {
            System.out.println("About the person:");
            Person.showPersonDetails(selPerson);
        }
    }

    public static void showTenantActions() {
        System.out.println("1 - register person");
        System.out.println("2 - check out person");
        System.out.println("3 - put thing");
        System.out.println("4 - remove thing");
        System.out.println("5 - renew rental");
        System.out.println("6 - cancel rental");
        System.out.println("7 - about the person");
    }

    public static Person selPerson(Place selPlace) {
        System.out.println("Living persons in " + selPlace.name + "\n");
        for (int i = 0; i < selPlace.livingPersons.size(); i++)
            System.out.println(i + " - " + selPlace.livingPersons.get(i));
        int choice = Main.scan.nextInt();

        return selPlace.livingPersons.get(choice);
    }

    public static void putTh(Person selTenant) {
        Parking selParking = Parking.selParking(selTenant);
        Thing selThing = Thing.selThing(selParking);
        selTenant.putThing(selThing, selParking);
    }

    public void putThing(Thing thing, Parking parking) {
        if (isTenant && rentedPlaces.contains(parking) && rentedPlaces.size() <= 5) {
            if (parking.storedThings.contains(thing))
                System.out.println("Thing " + thing.getName() + " is already exists");
            else {
                parking.storedThings.add(thing);
                System.out.println("Thing " + thing.getName() + " is successfully added");
            }
        } else
            System.out.println("You don't have possibility to put the thing");
    }

    public static void removeTh(Person selTenant) {
        Parking selParking = Parking.selParking(selTenant);
        Thing selThing = Thing.selThing(selParking);
        selTenant.removeThing(selThing, selParking);
    }

    public static void tenantActions(Person selTenant, Place selPlace, int choice) {
        switch (choice) {
            case 1 -> Person.regPerson(selTenant, selPlace);
            case 2 -> Person.checkOutP(selTenant);
            case 3 -> Person.putTh(selTenant);
            case 4 -> Person.removeTh(selTenant);
            case 5 -> Person.renewRent(selTenant);
            case 6 -> Person.cancelRent(selTenant);
            case 7 -> Person.showPersonDetails(selTenant);
            default -> System.out.println("Entered digit does not have an action");
        }
    }

    public static void checkOutP(Person selTenant) {
        Apartment selPlace = Apartment.selApart(selTenant);
        Person person = selPerson(selPlace);
        selTenant.checkOutPerson(person, selPlace);
    }

    public static void regPerson(Person selTenant, Place selPlace) {
        System.out.print("Person:\n1 - create new ");
        if (Person.allExistingPersons.size() > 1)
            System.out.println("\n2 - add existing");
        int choice = Main.scan.nextInt();
/*
        System.out.println("Available apartments:\n");
        for (int i = 0; i < selTenant.rentedPlaces.size(); i++)
            System.out.println(i + " - " + selTenant.rentedPlaces.get(i));
        int choice2 = scan.nextInt();
        Place place = selTenant.rentedPlaces.get(choice2);
*/

        Person regPerson;
        if (choice == 1) {
            regPerson = Person.createPerson();
            selTenant.registerPerson(regPerson, selPlace);
            Person.allExistingPersons.add(regPerson);
        } else if (choice == 2 && Person.allExistingPersons.size() > 1) {
            regPerson = Person.findExistingPerson();
            selTenant.registerPerson(regPerson, selPlace);
        }
    }

    public static List<String> enterPersonData() {
        System.out.print("Enter person first name: ");
        String firstN = Main.scan.next();

        System.out.print("Enter person last name: ");
        String lastN = Main.scan.next();

        System.out.print("Enter person pesel: ");
        String pesel = Main.scan.next();

        System.out.print("Enter person birthday: ");
        String birthday = Main.scan.next();

        System.out.print("Enter person address: ");
        Main.scan.useDelimiter("\\s");
        String address = Main.scan.next();
        String address2 = Main.scan.next();

        String addressComplete = address + " " + address2;
        return Arrays.asList(firstN, lastN, pesel, birthday, addressComplete);
    }

    public static Person findExistingPerson() {
        List<String> personData = Person.enterPersonData();
        int same = 0;
        for (Person person : Person.allExistingPersons) {
            if (person.name.equals(personData.get(0) + " " + personData.get(1)))
                same++;
            if (person.pesel.equals(personData.get(2)))
                same++;
            if (person.birthday.equals(personData.get(3)))
                same++;
            if (person.address.equals(personData.get(4)))
                same++;
            if (same == 4)
                return person;
        }
        System.out.println("Entered person is not exists");
        return null;
    }

    public static Person createPerson() {
        List<String> personData = Person.enterPersonData();
        return new Person(
                personData.get(0), personData.get(1), personData.get(2), personData.get(3), personData.get(4)
        );
    }

    public static void cancelRent(Person selTenant) {
        Place place = Apartment.selApart(selTenant);
        selTenant.cancelRental(place);
    }

    public static void renewRent(Person selTenant) {
        Place place = Apartment.selApart(selTenant);
        System.out.println("Enter rent end:");
        String rentEnd = Main.scan.next();
        selTenant.renewRental(place, rentEnd);
    }

    private static void showPersonDetails(Person selPerson) {
        System.out.println("Id: " + selPerson.id);
        System.out.println("Name: " + selPerson.name);
        System.out.println("Is tenant: " + selPerson.isTenant);
        if (selPerson.rentedPlaces.isEmpty())
            System.out.println("Rented places: none");
        else
            System.out.println("Rented places: " + selPerson.rentedPlaces.toString());
        System.out.println("Address: " + selPerson.address);
        System.out.println("Pesel: " + selPerson.pesel);
        System.out.println("Address: " + selPerson.address);
        System.out.println("Birthday: " + selPerson.birthday);
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

    public String getName() {
        return name;
    }

    public String getPesel() {
        return pesel;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getId() {
        return id;
    }

    public static int getIncrementId() {
        return incrementId;
    }

    public boolean isTenant() {
        return isTenant;
    }

    public List<Place> getRentedPlaces() {
        return rentedPlaces;
    }

    public List<TenantLetter> getLetters() {
        return letters;
    }

    public static List<Person> getAllExistingPersons() {
        return allExistingPersons;
    }

}
