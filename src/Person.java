import java.time.LocalDate;
import java.util.*;

public class Person {
    private final String name;
    private final String pesel;
    private final String address;
    private final String birthday;
    private final int id;
    private static int incrementId = 1;
    private boolean isTenant;
    private List<Place> rentedPlaces = new ArrayList<>();
    private List<TenantLetter> letters = new ArrayList<>();
    private static List<Person> allExistingPersons = new ArrayList<>();
    public boolean rentAfterTime = false;

    public Person(String firstName, String lastName, String pesel, String birthday, String address) {
        this.pesel = pesel;
        this.birthday = birthday;
        this.id = incrementId++;
        this.name = firstName + " " + lastName;
        this.address = address;
    }

    private void registerPerson(Person person, Place place) {
        if (isTenant) {
            if (place.checkLivingPersonExists(person))
                System.out.println("Person " + person.name + " is already register");
            else {
                place.addPersonToPlace(person);
                Person.allExistingPersons.add(person);
                System.out.println("Person " + person.name + " is successfully added");
            }
        } else
            System.out.println("You don't have possibility to register the person");
    }

    public void checkOutPerson(Apartment selApart) {
        if (selApart != null) {
            Person selPerson = selApart.selectPerson();
            if (selPerson != null && rentedPlaces.contains(selApart)) {
                if (selApart.checkLivingPersonExists(selPerson)) {
                    selApart.removePersonToPlace(selPerson);
                    Person.allExistingPersons.remove(selPerson);
                    System.out.println("Person " + selPerson.name + " is successfully removed");
                } else
                    System.out.println("Person " + selPerson.name + " is already register");
            } else
                System.out.println("You do not have persons in your rent apartment");
        }
        else
            System.out.println("You do not rent any apartment");
    }

    public void removeThing(Parking selParking) {
        if (selParking != null) {
            Thing selThing = selParking.showStoredThings();
            if (isTenant && rentedPlaces.contains(selParking)) {
                if (selParking.checkStoredThingExists(selThing)) {
                    selParking.removeStoredThing(selThing);
                    System.out.println("Thing " + selThing.getName() + " is successfully removed");
                } else
                    System.out.println("Thing " + selThing.getName() + " is not exists");
            } else
                System.out.println("You don't have possibility to put the thing");
        } else
            System.out.println("Entered parking does not exists");
    }

    protected void renewRental(Place selPlace) {
        LocalDate rentEnd = this.enterPersonRentEnd();
        selPlace.setRentEnd(rentEnd);
        selPlace.setRentStart();
        this.clearExistingLettersFor(selPlace);
        this.rentAfterTime = true;
        System.out.println("Successfully renew rent of place: " + selPlace.getName());
    }

    public void cancelRental(Place selPlace) {
        if (this.rentedPlaces.contains(selPlace)) {
            this.clearExistingLettersFor(selPlace);
            selPlace.clearPlace();
            System.out.println("Rental is successfully canceled");
        } else
            System.out.println("You do not rent this apartment");
    }

    private void clearExistingLettersFor(Place selPlace) {
        for (TenantLetter tenantLetter : this.letters)
            if (tenantLetter.getPlaceName().hashCode() == selPlace.hashCode())
                this.removeLetter(tenantLetter);
    }

    public Apartment selApart() {
        if (this.rentedPlacesSize() > 0) {
            System.out.println("Select apartment: ");
            for (int i = 0; i < this.rentedPlacesSize(); i++)
                if (this.getRentedPlace(i) instanceof Apartment)
                    System.out.println(i + " - " + this.getRentedPlace(i).getName());
            int choice = Main.getScan().nextInt();

            return (Apartment) this.getRentedPlace(choice);
        }
        return null;
    }

    public void selPersonActions(Place selPlace) {
        System.out.println("Selected person: " + this.getName());
        if (this.isTenant) {
            Person.showTenantActions(selPlace);
            int choice = Main.getScan().nextInt();
            this.tenantActions(selPlace, choice);
        } else {
            System.out.println("About the person:");
            this.showPersonDetails();
        }
    }

    public void putThing(Parking selParking) {
        Thing selThing = Thing.createThing();

        if (selThing != null && isTenant && rentedPlaces.contains(selParking) && rentedPlaces.size() <= 5) {
            if (selParking.checkStoredThingExists(selThing))
                System.out.println("Thing " + selThing.getName() + " is already exists");
            else {
                selParking.storeThing(selThing);
                System.out.println("Thing " + selThing.getName() + " is successfully added");
            }
        } else
            System.out.println("You don't have possibility to put the thing");
    }

    public void regPerson(Place selPlace) {
        System.out.print("Person:\n1 - create new ");
        if (Person.allExistingPersons.size() > 1)
            System.out.println("\n2 - add existing");
        int choice = Main.getScan().nextInt();

        Person regPerson;
        if (choice == 1) {
            regPerson = Person.createPerson();
            this.registerPerson(regPerson, selPlace);
        } else if (choice == 2 && Person.allExistingPersons.size() > 1) {
            regPerson = Person.findExistingPerson();
            if (regPerson != null)
                this.registerPerson(regPerson, selPlace);
        }
    }

    public static void checkSpecDateWithCurr(String typeOfDate, String dateStr) {
        if (typeOfDate.equals("rent end") && LocalDate.parse(dateStr).isBefore(DateUpdater.getCurrDate()))
            throw new ProblematicDateException("Cannot rent place which has date of rent end less than current date");
        else if (typeOfDate.equals("birthday") && LocalDate.parse(dateStr).isAfter(DateUpdater.getCurrDate()))
            throw new ProblematicDateException("Entered birthday is greater than current date");
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
        Person.checkSpecDateWithCurr("birthday", birthday);

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
        Person.checkSpecDateWithCurr("rent end", rentEnd);
        return LocalDate.parse(rentEnd);
    }

    public static Person createPerson() {
        List<String> personData = Person.enterPersonData();
        return new Person(
                personData.get(0), personData.get(1), personData.get(2), personData.get(3), personData.get(4)
        );
    }

    public String displayLetters() {
        if (this.isTenant && this.letters != null) {
            String lettersList = "";
            for (int i = 0; i < this.letters.size(); i++)
                lettersList += i + " - " + this.readSpecificLetter(i) + "\n";
            if (lettersList.isEmpty())
                return "none\n";
            else
                return "\n" + lettersList;
        } else
            return "not tenant";
    }

    public void showPersonDetails() {
        System.out.println("Id: " + this.id);
        System.out.println("Name: " + this.name);
        System.out.println("Is tenant: " + this.isTenant);
        System.out.println("Address: " + this.address);
        System.out.println("Pesel: " + this.pesel);
        System.out.println("Birthday: " + this.birthday);
        System.out.println("Rented places: " + this.getRentedPlaces());
        System.out.print("Tenant letters details: " + this.displayLetters());
    }

    public String getRentedPlaces() {
        String tempRentedPlaces = "";
        Place[] rentedPlaces = this.getSortedPlaces();
        if (this.rentedPlacesSize() > 0)
            for (int i = 0; i < this.rentedPlacesSize(); i++)
                tempRentedPlaces += rentedPlaces[i].showRentedPlaceDetails();
        else
            return "none";
        return tempRentedPlaces;
    }

    private Place[] getSortedPlaces() {
        Place[] sortedPlaces = new Place[this.rentedPlacesSize()];
        if (this.rentedPlacesSize() > 1)
            for (int i = 0; i < this.rentedPlacesSize() - 1; i++) {
                System.out.println(this.rentedPlaces.get(i).getName());
                for (int j = i + 1; j < this.rentedPlacesSize(); j++)
                    if (this.getRentedPlace(i).volume > this.getRentedPlace(j).volume) {
                        sortedPlaces[i] = this.getRentedPlace(j);
                        sortedPlaces[j] = this.getRentedPlace(i);
                    } else {
                        sortedPlaces[i] = this.getRentedPlace(i);
                        sortedPlaces[j] = this.getRentedPlace(j);
                    }
            } else if (this.rentedPlacesSize() == 1)
                sortedPlaces[0] = this.getRentedPlace(0);
        return sortedPlaces;
    }

    public static void showAllExistingTenants() {
        System.out.println("Existing tenants:");
        for (int i = 0; i < Person.allExistingPersonsSize(); i++)
            if (Person.allExistingPersons.get(i).isTenant)
                System.out.println(i + " - " + Person.allExistingPersons.get(i).name);
    }

    public static int allExistingTenantsSize() {
        int count = 0;
        for (int i = 0; i < Person.allExistingPersonsSize(); i++)
            if (Person.allExistingPersons.get(i).isTenant)
                count++;
        return count;
    }

    public static void readyPersons() {
        Person person = new Person("Sebastian", "Wieczorek", "1236432212", "2002-11-30", "Przasnyska 23a");
        Person person2 = new Person("Damian", "Mołdawski", "423423234", "2002-09-23", "Kwiatkowskiego 1c");
        Person person3 = new Person("Robert", "Kowalski", "1231232132", "2005-05-15", "Bogusławskiego 3");
        Person person4 = new Person("Lidia", "Szczypior", "543342158", "2002-12-11", "Powązkowska 25b");
        Person person5 = new Person("Piotr", "Wróbel", "1247964332", "2003-07-21", "Maczka 43");
        Person.addPersonToExisting(List.of(person, person2, person3, person4, person5));
    }

    private String readSpecificLetter(int index) {
        return this.getLetter(index).read();
    }

    private void tenantActions(Place selPlace, int choice) {
        selPlace.placeActions(this, choice);
    }

    public static void showTenantActions(Place place) {
        place.showPlaceActions();
    }

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

    public static Person getExistingPerson(int index) {
        return Person.allExistingPersons.get(index);
    }

    public static void addPersonToExisting(Person person) {
        Person.allExistingPersons.add(person);
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

    public void removeRentedPlace(Place place) {
        this.rentedPlaces.remove(place);
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

    public int lettersCount() {
        return this.letters.size();
    }

    public TenantLetter getLetter(int index) {
        return this.letters.get(index);
    }

}
