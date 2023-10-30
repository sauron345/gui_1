import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

class Main extends Thread {

    public static int i = 1;
    public static LocalDate currDate;
    public static List<Place> places;
    public static List<Person> persons;
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws TooManyThingsException {
        Main.currDate = LocalDate.now();

        Developer dev = new Developer("Patryk");
        Estate estate = new Estate("Żoliborz");
        dev.addEstate(estate);

        Person person = new Person("Sebastian", "Wieczorek", "1236432212", "2002-11-30", "Przasnyska 23a");
        Person person2 = new Person("Damian", "Mołdawski", "423423234", "2002-09-23", "Kwiatkowskiego 1c");
        Person person3 = new Person("Robert", "Kowalski", "1231232132", "2005-05-15", "Bogusławskiego 3");
        Person person4 = new Person("Lidia", "Szczypior", "543342158", "2002-12-11", "Powązkowska 25b");
        Person person5 = new Person("Piotr", "Wróbel", "1247964332", "2003-07-21", "Maczka 43");

        persons = Arrays.asList(
            person, person2, person3, person4, person5
        );
        Person.allExistingPersons.addAll(persons);

        Apartment apartment = new Apartment("room1", 42);
        Apartment apartment2 = new Apartment("room2", 54);
        Apartment apartment3 = new Apartment("room3", 49);
        Apartment apartment4 = new Apartment("room4", 66);
        Apartment apartment5 = new Apartment("room5", 54);
        Apartment apartment6 = new Apartment("room6",56);

        Parking parking = new Parking("parking1", 30);
        Parking parking2 = new Parking("parking2", 54);
        Parking parking3 = new Parking("parking3", 24);
        Parking parking4 = new Parking("parking4", 32);

        places = Arrays.asList(
            apartment, apartment2, apartment3, apartment4, apartment5, apartment6,
            parking, parking2, parking3, parking4
        );
        estate.addPlace(places);

        Main main = new Main();

        Main.loop(main, scan, dev);
    }

    private static Estate createEstate() {
        List<String> placeData = Main.enterEstateName();
        return new Estate(placeData.get(0));
    }

    private static List<String> enterEstateName() {
        System.out.print("Enter estate name: ");
        String name = Main.scan.next();

        return Arrays.asList(name);
    }

    private static void loop(Main main, Scanner scan, Developer dev) throws TooManyThingsException {
        int choice;

        while (true) {
            main.run();

            System.out.println("\nSelect estate:");
            System.out.println("20 - add estate");
            if (Estate.allExistingEstates.size() > 0) {
                System.out.println("21 - remove estate");
                System.out.println("------------------");
                System.out.println("Existing estates:");
            }
            for (int i = 0; i < dev.estates.size(); i++)
                System.out.println(i + " - " + dev.estates.get(i).name);
            choice = scan.nextInt();

            Estate selEstate;
            if (choice == 20) {
                selEstate = Main.createEstate();
                dev.addEstate(selEstate);
            } else if (choice == 21 && Estate.allExistingEstates.size() > 0) {
                selEstate = Main.findExistingEstate();
                if (selEstate != null)
                    dev.removeEstate(selEstate);
                continue;
            } else
                selEstate = dev.estates.get(choice);

            System.out.println("\nSelect place of " + selEstate.name + ":");
            if (selEstate.places.isEmpty())
                System.out.println("No places available yet");

            System.out.println("20 - Add new place");
            if (Estate.allExistingEstates.size() > 0) {
                System.out.println("21 - remove place");
                System.out.println("------------------");
                System.out.println("Existing places:");
            }
            for (int i = 0; i < selEstate.places.size(); i++)
                System.out.println(i + " - " + selEstate.places.get(i).name);
            choice = scan.nextInt();
            Place selPlace;
            if (choice == 20) {
                Place placeCreated = Main.createPlace();
                selPlace = selEstate.addPlace(placeCreated);
            } else if (choice == 21 && Estate.allExistingEstates.size() > 0) {
                selPlace = Main.findExistingPlace();
                if (selPlace != null)
                    selEstate.removePlace(selPlace);
                continue;
            } else
                selPlace = selEstate.places.get(choice);

            if (selPlace instanceof Apartment) {
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
                choice = scan.nextInt();
                Person selPerson;
                if (choice == 15) {
                    Main.showPlaceDetails(selPlace);
                } else if (selPlace.livingPersons.isEmpty() && choice == 9) {
                    Main.addApartTenant((Apartment) selPlace);
                } else {
                    selPerson = selPlace.livingPersons.get(choice);
                    System.out.println("Selected person: " + selPerson.name);
                    Main.selPersonActions(selPerson, selPlace);
                }
            } else if (selPlace instanceof Parking) {
                System.out.println("\nSelect thing stored in " + selPlace.name + ":");
                System.out.println("15 - Parking details");
                if (selPlace.tenant == null)
                    System.out.println("9 - Add tenant");
                else if (((Parking) selPlace).storedThings.isEmpty()) {
                    System.out.println("Nothing stored yet");
                } else {
                    System.out.println("------------------");
                    System.out.println("Tenant - " + selPlace.tenant.name);
                    System.out.println("20 - Add thing");
                    for (int i = 0; i < ((Parking) selPlace).storedThings.size(); i++)
                        System.out.println(i + " - " + ((Parking) selPlace).storedThings.get(i).name);
                }
                choice = scan.nextInt();
                Thing selThing;
                if (choice == 15) {
                    Main.showPlaceDetails(selPlace);
                } else if (choice == 9)
                    Main.addParkingTenant((Parking) selPlace);
                else if (choice == 20)
                    Main.addThing((Parking) selPlace);
                else {
                    selThing = ((Parking) selPlace).storedThings.get(choice);
                    System.out.println("Selected thing: " + selThing.name);
                    Main.showThingDetails(selThing);
                }
            }
            Main.i++;
        }
    }

    private static Place findExistingPlace() {
        List<String> estateData = Main.enterPlaceName();
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

    private static List<String> enterPlaceName() {
        System.out.print("Enter place name: ");
        String name = Main.scan.next();

        System.out.print("Enter place volume: ");
        String volume = Main.scan.next();

        return Arrays.asList(name, volume);
    }

    private static void addParkingTenant(Parking selPlace) {
        System.out.print("Tenant:\n1 - create new ");
        if (Person.allExistingPersons.size() > 0)
            System.out.println("\n2 - add existing");
        int choice = scan.nextInt();
        Person regPerson;
        if (choice == 1) {
            regPerson = Main.createPerson();
            regPerson.isTenant = true;
            Person.allExistingPersons.add(regPerson);

            System.out.print("Enter person rent end: ");
            String rentEnd = Main.scan.next();
            selPlace.startPlaceRental(regPerson, LocalDate.parse(rentEnd));
        } else if (choice == 2 && Person.allExistingPersons.size() > 0) {
            regPerson = Main.findExistingPerson();
            if (regPerson != null) {
                regPerson.isTenant = true;
                selPlace.startPlaceRental(regPerson, LocalDate.parse("20/12/2003"));
            }
        }
    }


    private static void showThingDetails(Thing selThing) {
        System.out.println("Name: " + selThing.name);
        System.out.println("Area: " + selThing.area);

        if (selThing instanceof Vehicle) {
            System.out.println("Vehicle type: " + ((Vehicle) selThing).vehicleType);
            System.out.println("Engine type: " + ((Vehicle) selThing).engineType);
            System.out.println("Engine capacity: " + ((Vehicle) selThing).engineCapacity);
            if (Vehicle.soldVehicles.isEmpty())
                System.out.println("Sold vehicles: none");
            else
                System.out.println("Sold vehicles: " + Vehicle.soldVehicles.toString());

        } else {
            if (Thing.thingsToUtilization.isEmpty())
                System.out.println("Things to utilization: none");
            else
                System.out.println("Things to utilization: " + Thing.thingsToUtilization.toString());
        }
    }

    private static void showPlaceDetails(Place selPlace) {
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

    private static void addThing(Parking selParking) throws TooManyThingsException {
        System.out.print("Thing:\n1 - create new ");
        if (Thing.allExistingThings.size() > 0)
            System.out.print("\n2 - add existing: ");
        int choice = scan.nextInt();
        Thing storeThing;
        if (choice == 1) {
            storeThing = Main.createThing();
            if (storeThing != null) {
                selParking.storedThings.add(storeThing);
                Thing.allExistingThings.add(storeThing);
            }
        } else if (choice == 2 && Thing.allExistingThings.size() > 0) {
            storeThing = Main.findExistingThing();
            if (storeThing != null)
                selParking.storeThing(storeThing);
        }
    }

    private static Thing findExistingThing() {
        List<String> ThingData = Main.enterThingData();
        int same = 0;
        for (Thing thing : Thing.allExistingThings) {
            if (thing.name.equals(ThingData.get(0) + " " + ThingData.get(1)))
                same++;
            if (thing.area == Integer.parseInt(ThingData.get(2)))
                same++;
            if (same == 2)
                return thing;
        }
        System.out.println("Entered thing is not exists");
        return null;
    }

    private static void addApartTenant(Apartment selPlace) {
        System.out.print("Tenant:\n1 - create new ");
        if (Person.allExistingPersons.size() > 0)
            System.out.println("\n2 - add existing");
        int choice = scan.nextInt();
        Person regPerson;
        if (choice == 1) {
            regPerson = Main.createPerson();
            regPerson.isTenant = true;
            Person.allExistingPersons.add(regPerson);
            selPlace.assignRental(regPerson);
        } else if (choice == 2 && Person.allExistingPersons.size() > 0) {
            regPerson = Main.findExistingPerson();
            if (regPerson != null) {
                regPerson.isTenant = true;
                selPlace.assignRental(regPerson);
            }
        }
    }

    private static void selPersonActions(Person selPerson, Place selPlace) {
        int choice;
        if (selPerson.isTenant) {
            Main.showTenantActions();
            choice = scan.nextInt();
            Main.tenantActions(selPerson, selPlace, choice);
        } else {
            System.out.println("About the person:");
            Main.showPersonDetails(selPerson);
        }
    }

    private static void showTenantActions() {
        System.out.println("1 - register person");
        System.out.println("2 - check out person");
        System.out.println("3 - put thing");
        System.out.println("4 - remove thing");
        System.out.println("5 - renew rental");
        System.out.println("6 - cancel rental");
        System.out.println("7 - about the person");
    }

/*    private static Estate selEstate(List<Estate> estates) {
        System.out.println("\nSelect estate:");
        for (int i = 0; i < estates.size(); i++)
            System.out.println(i + " - " + estates.get(i).name);
        int choice = scan.nextInt();
        return estates.get(choice);
    }

    private static Place selPlaceOf(Estate selEstate) {
        System.out.println("\nSelect place of " + selEstate.name + ":");
        for (int i = 0; i < selEstate.places.size(); i++)
            System.out.println(i + " - " + selEstate.places.get(i).name);
        int choice = scan.nextInt();
        return selEstate.places.get(choice);
    }


    public static void selPerson(Place place) {
        System.out.println("\nPersons living in " + place.name + " currently:");
        if (place.livingPersons.isEmpty()) {
            System.out.println("No one\n");
            Main.loop();
        } else {
            for (int i = 0; i < place.livingPersons.size(); i++)
                System.out.println(i + " - " + place.livingPersons.get(i).name);
            int choice = scan.nextInt();
        }
    }*/

    public static void tenantActions(Person selTenant, Place selPlace, int choice) {
        switch (choice) {
            case 1 -> Main.regPerson(selTenant, selPlace);
            case 2 -> Main.checkOutP(selTenant);
            case 3 -> Main.putTh(selTenant);
            case 4 -> Main.removeTh(selTenant);
            case 5 -> Main.renewRent(selTenant);
            case 6 -> Main.cancelRent(selTenant);
            case 7 -> Main.showPersonDetails(selTenant);
            default -> System.out.println("Entered digit does not have an action");
        }
    }

    private static void cancelRent(Person selTenant) {
        Place place = selApart(selTenant);
        selTenant.cancelRental(place);
    }

    private static void renewRent(Person selTenant) {
        Place place = selApart(selTenant);
        System.out.println("Enter rent end:");
        String rentEnd = scan.next();
        selTenant.renewRental(place, rentEnd);
    }

    private static void removeTh(Person selTenant) {
        Parking selParking = selParking(selTenant);
        Thing selThing = Main.selThing(selParking);
        selTenant.removeThing(selThing, selParking);
    }

    private static void putTh(Person selTenant) {
        Parking selParking = Main.selParking(selTenant);
        Thing selThing = Main.selThing(selParking);
        selTenant.putThing(selThing, selParking);
    }

    private static Thing selThing(Parking selParking) {
        System.out.println("Stored things in " + selParking.name + "\n");
        for (int i = 0; i < selParking.storedThings.size(); i++) {
            if (selParking.storedThings.get(i) instanceof Vehicle)
                System.out.println(i + " - (Vehicle) " + selParking.storedThings.get(i));
            else
                System.out.println(i + " - " + selParking.storedThings.get(i));
        }
        int choice2 = scan.nextInt();
        return selParking.storedThings.get(choice2);
    }

    private static Parking selParking(Person selTenant) {
        System.out.println("Select parking:\n");
        for (int i = 0; i < selTenant.rentedPlaces.size(); i++)
            if (selTenant.rentedPlaces.get(i) instanceof Parking)
                System.out.println(i + " - " + selTenant.rentedPlaces.get(i));
        int choice = scan.nextInt();

        return (Parking) selTenant.rentedPlaces.get(choice);
    }

    private static Apartment selApart(Person selTenant) {
        System.out.print("Select apartment: ");
        for (int i = 0; i < selTenant.rentedPlaces.size(); i++)
            if (selTenant.rentedPlaces.get(i) instanceof Apartment)
                System.out.println(i + " - " + selTenant.rentedPlaces.get(i));
        int choice = scan.nextInt();

        return (Apartment) selTenant.rentedPlaces.get(choice);
    }

    private static Person selPerson(Place selPlace) {
        System.out.println("Living persons in " + selPlace.name + "\n");
        for (int i = 0; i < selPlace.livingPersons.size(); i++)
            System.out.println(i + " - " + selPlace.livingPersons.get(i));
        int choice = scan.nextInt();

        return selPlace.livingPersons.get(choice);
    }

    private static void checkOutP(Person selTenant) {
        Apartment selPlace = selApart(selTenant);
        Person person = selPerson(selPlace);
        selTenant.checkOutPerson(person, selPlace);
    }

    private static void regPerson(Person selTenant, Place selPlace) {
        System.out.print("Person:\n1 - create new ");
        if (Person.allExistingPersons.size() > 1)
            System.out.println("\n2 - add existing");
        int choice = scan.nextInt();
/*
        System.out.println("Available apartments:\n");
        for (int i = 0; i < selTenant.rentedPlaces.size(); i++)
            System.out.println(i + " - " + selTenant.rentedPlaces.get(i));
        int choice2 = scan.nextInt();
        Place place = selTenant.rentedPlaces.get(choice2);
*/

        Person regPerson;
        if (choice == 1) {
            regPerson = Main.createPerson();
            selTenant.registerPerson(regPerson, selPlace);
            Person.allExistingPersons.add(regPerson);
        } else if (choice == 2 && Person.allExistingPersons.size() > 1) {
            regPerson = Main.findExistingPerson();
            selTenant.registerPerson(regPerson, selPlace);
        }
    }

    private static Estate findExistingEstate() {
        List<String> estateData = Main.enterEstateName();
        for (Estate estate : Estate.allExistingEstates)
            if (estate.name.equals(estateData.get(0)))
                return estate;
        System.out.println("Entered estate is not exists");
        return null;
    }


    private static List<String> enterThingData() {
        System.out.print("Enter thing name: ");
        String name = Main.scan.next();

        System.out.print("Enter thing type (Vehicle or Thing): ");
        String type = Main.scan.next();

        System.out.println("Enter:\n1 - volume\n2 - width, height, length");
        int choice = Main.scan.nextInt();
        String volume = Main.enterVolumeData(choice);

        return Arrays.asList(name, volume, type);
    }

    private static List<String> enterPersonData() {
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

    private static Person findExistingPerson() {
        List<String> personData = Main.enterPersonData();
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

    private static Person createPerson() {
        List<String> personData = Main.enterPersonData();
        return new Person(
            personData.get(0), personData.get(1), personData.get(2), personData.get(3), personData.get(4)
        );
    }

    private static Thing createThing() {
        List<String> personData = Main.enterThingData();
        if (personData.get(2).equals("Thing")) {
            return new Thing(personData.get(0), Integer.parseInt(personData.get(1)));
        } else if (personData.get(2).equals("Vehicle")) {
            System.out.print("Enter engine capacity: ");
            int engineCapacity = Main.scan.nextInt();

            System.out.print("Enter vehicle type (available: " + Arrays.toString(Vehicle.availableTypes) +"): ");
            String vehicleType = Main.scan.next();

            System.out.print("Enter engine type: ");
            String engineType = Main.scan.next();

            return new Vehicle(personData.get(0), Integer.parseInt(personData.get(1)), engineCapacity, vehicleType, engineType);
        }
        return null;
    }

    private static List<String> enterPlaceData() {
        System.out.print("Enter place name: ");
        String name = Main.scan.next();

        System.out.println("Enter:\n1 - volume\n2 - width, height, length");
        int choice = Main.scan.nextInt();
        String volume = Main.enterVolumeData(choice);

        System.out.print("Enter type place (Parking or Apartment): ");
        String type = Main.scan.next();

        return Arrays.asList(name, volume, type);
    }

    private static String enterVolumeData(int choice) {
        int volume = 0;
        if (choice == 1) {
            System.out.print("Enter volume: ");
            volume = Main.scan.nextInt();
        } else if (choice == 2) {
            System.out.print("Enter width, height, length (include comma) ");
            String[] volumeParams = Main.scan.next().split(",");
            volume = Integer.parseInt(volumeParams[0]) * Integer.parseInt(volumeParams[1]) * Integer.parseInt(volumeParams[2]);
        }
        return String.valueOf(volume);
    }

    private static Place createPlace() {
        List<String> placeData = Main.enterPlaceData();
        if (placeData.get(2).equals("Apartment"))
            return new Apartment(placeData.get(0), Integer.parseInt(placeData.get(1)));
        else if (placeData.get(2).equals("Parking"))
            return new Parking(placeData.get(0), Integer.parseInt(placeData.get(1)));
        return null;
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

   /* private static Object availableOptions(Scanner scan, List<?> list, int choice) {
        try {
            choice = scan.nextInt();
            selEstate = estates.get(choice);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Selected digit has nothing to offer");
            continue;
        }
    }*/

    @Override
    public void run() {
        try {
            System.out.println("\n"+currDate);
            Thread.sleep(5000);
            currDate = Main.incrementDate();
            if (Main.i % 2 == 0)
                Main.checkRentedPlacesValidity();
        } catch (InterruptedException e ) {
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static LocalDate incrementDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(String.valueOf(Main.currDate)));
        c.add(Calendar.DATE, 1);
        return LocalDate.parse(sdf.format(c.getTime()));
    }

    private static void checkRentedPlacesValidity() throws ParseException {
        for (int i = 0; i < Place.allRentedPlaces.size(); i++) {
            Person tenant = Place.allRentedPlaces.get(i).tenant;
            if (Place.allRentedPlaces.get(i).rentEnd.isAfter(Main.currDate))
                Main.rentedPlaceAfterTime(i, tenant);
            for (int j = 0; j < tenant.letters.size(); j++) {
                if (tenant.letters.get(j).receivedDate.compareTo(Main.currDate) > 30) {
                    tenant.letters.get(j).place.clearPlace();
                }
            }
        }
    }

    private static void rentedPlaceAfterTime(int i, Person tenant) {
        tenant.letters.add(
            new TenantLetter(tenant.name, Place.allRentedPlaces.get(i)
        ));
        Place.allRentedPlaces.get(i).rentEnd = null;
    }

}
