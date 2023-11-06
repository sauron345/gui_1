import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

public class Person {
    private String name;
    private String pesel;
    private String address;
    private String birthday;
    private int id;
    private static int incrementId = 1;
    private boolean isTenant;
    private List<Place> rentedPlaces = new ArrayList<>();
    private List<TenantLetter> letters = new ArrayList<>();
    private static List<Person> allExistingPersons = new ArrayList<>();

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
                if (place.checkLivingPersonExists(person))
                    System.out.println("Person " + person.name + " is already register");
                else {
                    place.addPersonToPlace(person);
                    Person.allExistingPersons.add(person);
                    System.out.println("Person " + person.name + " is successfully added");
                }
            } else
                System.out.println("Place is not exists or rented places are greater than 5");
        } else
            System.out.println("You don't have possibility to register the person");
    }

    private void checkOutPerson(Person person, Apartment place) {
        if (isTenant && rentedPlaces.contains(place)) {
            if (place.checkLivingPersonExists(person)) {
                place.removePersonToPlace(person);
                Person.allExistingPersons.remove(person);
                System.out.println("Person " + person.name + " is successfully removed");
            } else
                System.out.println("Person " + person.name + " is already register");
        } else
            System.out.println("You don't have possibility to check out the person");
    }

    public static void showPersonsLiving(Place selPlace) {
        System.out.println("\nSelect person living in " + selPlace.getName() + " :");
        System.out.println("15 - Apartment details");
        if (selPlace.checkLivingPersonsIsEmpty()) {
            System.out.println("No one");
            System.out.println("9 - Add tenant");
        } else {
            System.out.println("------------------");
            System.out.println("Living persons:");
            for (int i = 0; i < selPlace.livingPersonsSize(); i++) {
                if (selPlace.getLivingPerson(i).isTenant)
                    System.out.println(i + " - (Tenant) " + selPlace.getLivingPerson(i).name);
                else
                    System.out.println(i + " - " + selPlace.getLivingPerson(i).name);
            }
        }

    }

    public static void selPersonLiving(int choice, Place selPlace) {
        Person selPerson;
        if (choice == 15) {
            Place.showPlaceDetails(selPlace);
        } else if (selPlace.checkLivingPersonsIsEmpty() && choice == 9) {
            Apartment.addApartTenant((Apartment) selPlace);
        } else {
            selPerson = selPlace.getLivingPerson(choice);
            System.out.println("Selected person: " + selPerson.name);
            Person.selPersonActions(selPerson, selPlace);
        }
    }

    public void removeThing(Thing thing, Parking parking) {
        if (isTenant && rentedPlaces.contains(parking)) {
            if (parking.checkStoredThingExists(thing)) {
                parking.removeStoredThing(thing);
                System.out.println("Thing " + thing.getName() + " is successfully removed");
            } else
                System.out.println("Thing " + thing.getName() + " is not exists");
        } else
            System.out.println("You don't have possibility to put the thing");
    }

    public void renewRental(Place place, LocalDate rentEnd) {
        if (this.isTenant && this.rentedPlaces.contains(place)) {
            for (TenantLetter tenantLetter : place.getTenant().letters) {
                if (tenantLetter.getPlace() == place && tenantLetter.getName().equals(place.getTenant().name)) {
                    place.setRentEnd(rentEnd);
                    place.setRentStart();
                    this.letters.remove(tenantLetter);
                }
            }
        } else if (this.isTenant) {
            place.setRentStart();
            place.setRentEnd(rentEnd);
        }
    }

    public void cancelRental(Place place) {
        if (this.isTenant && this.rentedPlaces.contains(place)) {
            for (TenantLetter tenantLetter : place.getTenant().letters)
                if (tenantLetter.getPlace() == place && tenantLetter.getName().equals(place.getTenant().name))
                    this.removeLetter(tenantLetter);
//            place.rentStart = null;
            place.setRentEnd(Main.getCurrDate());
        }
    }

    public static void selPersonActions(Person selPerson, Place selPlace) {
        if (selPerson.isTenant) {
            Person.showTenantActions();
            int choice = Main.getScan().nextInt();
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
        System.out.println("Living persons in " + selPlace.getName());
        for (int i = 0; i < selPlace.livingPersonsSize(); i++)
            System.out.println(i + " - " + selPlace.getLivingPerson(i).getName());
        int choice = Main.getScan().nextInt();

        return selPlace.getLivingPerson(choice);
    }

    public static void putTh(Person selTenant) {
        Parking selParking = Parking.selParking(selTenant);
        Thing selThing = Thing.selThing(selParking);
        selTenant.putThing(selThing, selParking);
    }

    public void putThing(Thing thing, Parking parking) {
        if (isTenant && rentedPlaces.contains(parking) && rentedPlaces.size() <= 5) {
            if (parking.checkStoredThingExists(thing))
                System.out.println("Thing " + thing.getName() + " is already exists");
            else {
                parking.storeThing(thing);
                System.out.println("Thing " + thing.getName() + " is successfully added");
            }
        } else
            System.out.println("You don't have possibility to put the thing");
    }

    public static void removeTh(Person selTenant) {
        Parking selParking = Parking.selParking(selTenant);
        if (selParking != null) {
            Thing selThing = Thing.selThing(selParking);
            selTenant.removeThing(selThing, selParking);
        }
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
        if (selPlace != null) {
            Person person = selPerson(selPlace);
            selTenant.checkOutPerson(person, selPlace);
        }
    }

    public static void regPerson(Person selTenant, Place selPlace) {
        System.out.print("Person:\n1 - create new ");
        if (Person.allExistingPersons.size() > 1)
            System.out.println("\n2 - add existing");
        int choice = Main.getScan().nextInt();

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

    public static void checkDateWithCurr(String dateStr, String errMsg) {
        if (LocalDate.parse(dateStr).isBefore(Main.getCurrDate()))
            throw new ProblematicDateException(errMsg);
    }

    public static List<String> enterPersonData() {
        System.out.print("Enter person first name: ");
        String firstN = Main.getScan().next();

        System.out.print("Enter person last name: ");
        String lastN = Main.getScan().next();

        System.out.print("Enter person pesel: ");
        String pesel = Main.getScan().next();

        System.out.print("Enter person birthday (yyyy-mm-dd): ");
        String birthday = Main.getScan().next();
        Person.checkDateWithCurr(birthday, "Entered birthday is less than current date");

        System.out.print("Enter person address (whitespace required): ");
        Main.getScan().useDelimiter("\\s");
        String address = Main.getScan().next();
        String address2 = Main.getScan().next();

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

    public LocalDate enterPersonRentEnd() {
        System.out.print("Enter person rent end (yyyy-mm-dd): ");
        String rentEnd = Main.getScan().next();
        Person.checkDateWithCurr(rentEnd, "Cannot rent place which has date of rent end less than current date");
        return LocalDate.parse(rentEnd);
    }

    public static Person createPerson() {
        List<String> personData = Person.enterPersonData();
        return new Person(
                personData.get(0), personData.get(1), personData.get(2), personData.get(3), personData.get(4)
        );
    }

    public static void cancelRent(Person selTenant) {
        Place place = Apartment.selApart(selTenant);
        if (place != null)
            selTenant.cancelRental(place);
    }

    public static void renewRent(Person selTenant) {
        Place place = Apartment.selApart(selTenant);
        if (place != null) {
            LocalDate rentEnd = selTenant.enterPersonRentEnd();
            selTenant.renewRental(place, rentEnd);
        }
    }

    public void displayLetters() {
        if (this.isTenant) {
            System.out.println("Tenant letters details:");
            for (int i = 0; i < this.letters.size(); i++) {
                System.out.println(i + " " + this.letters.get(i).read());
            }
        }
    }

    public void displayLettersToFile(PrintWriter save) {
        if (this.isTenant && this.letters.size() > 0) {
            save.println("Tenant letters details:");
            for (int i = 0; i < this.letters.size(); i++) {
                save.println(i + " " + this.letters.get(i).read());
            }
        }
    }

    private static void showPersonDetails(Person selPerson) {
        System.out.println("Id: " + selPerson.id);
        System.out.println("Name: " + selPerson.name);
        System.out.println("Is tenant: " + selPerson.isTenant);
        selPerson.displayRentedPlaces();
        System.out.println("Address: " + selPerson.address);
        System.out.println("Pesel: " + selPerson.pesel);
        System.out.println("Address: " + selPerson.address);
        System.out.println("Birthday: " + selPerson.birthday);
        selPerson.displayLetters();
    }

    private void displayRentedPlaces() {
        if (this.isTenant) {
            System.out.println("Rented places: ");
            if (this.rentedPlaces.isEmpty()) {
                System.out.println("none");
            } else
                for (Place rentedPlace : this.rentedPlaces)
                    System.out.println("- " + rentedPlace);
        }
    }

    public void passRentedPlacesToFile(PrintWriter save) {
        save.print("Rented places: ");
        String instanceStr = "";
        if (this.rentedPlacesSize() > 0)
            for (int i = 0; i < this.rentedPlacesSize(); i++) {
                if (this.getRentedPlace(i) instanceof Apartment)
                    instanceStr = "Apartment";
                else
                    instanceStr = "Parking";
                save.println("\n- " + this.getRentedPlace(i).getName() + " ("+ instanceStr + ")");
            }
        else
            save.println("none");
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

    public void clearLetters() {
        this.letters = null;
    }

    public Place getRentedPlace(int index) {
        return this.rentedPlaces.get(index);
    }

    public int rentedPlacesSize() {
        return this.rentedPlaces.size();
    }

    public boolean checkRentedPlaceExists(Place place) {
        return this.rentedPlaces.contains(place);
    }

    public static int allExistingPersonsSize() {
        return Person.allExistingPersons.size();
    }

    public static void addPersonToExisting(Person person) {
        Person.allExistingPersons.add(person);
    }

    public static Person getExistingPerson(int index) {
        return Person.allExistingPersons.get(index);
    }

    public static void addPersonToExisting(List<Person> persons) {
        Person.allExistingPersons.addAll(persons);
    }

    public void setPersonAsTenant() {
        this.isTenant = true;
    }

    public void addLetter(TenantLetter letter) {
        this.letters.add(letter);
    }

    public void removeLetter(TenantLetter letter) {
        this.letters.remove(letter);
    }

    public void addRentedPlace(Place place) {
        this.rentedPlaces.add(place);
    }

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

    public int lettersSize() {
        return this.letters.size();
    }

    public TenantLetter getLetter(int index) {
        return this.letters.get(index);
    }

}
