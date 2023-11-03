import java.time.LocalDate;
import java.util.*;

class Main extends MultiThread {
    private static int i = 1;
    private static LocalDate currDate;
    private static List<Place> places;
    private static List<Person> persons;
    private static final Scanner scan = new Scanner(System.in);
    private static final MultiThread multiThread = new MultiThread();

    public static void main(String[] args) throws TooManyThingsException {
        Main main = new Main();

        Main.currDate = LocalDate.now();
        int choice;

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
        Person.addPersonToExisting(persons);

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

        while (true) {
             Main.multiThread.run();

            Estate.showEstateOptions(dev);
            choice = scan.nextInt();
            Estate selEstate = Estate.selEstateOption(choice, dev);

            Place.showPlaceOptions(selEstate);
            choice = scan.nextInt();
            Place selPlace = Place.selPlaceOption(choice, selEstate);

            if (selPlace instanceof Apartment) {
                Person.showPersonsLiving(selPlace);
                choice = scan.nextInt();

                Person selPerson = Person.selPersonLiving(choice, selPlace);

            } else if (selPlace instanceof Parking) {
                Parking.showStoredThings((Parking) selPlace);
                choice = scan.nextInt();

                Thing.selThingOption(choice, (Parking) selPlace);
            }
            Main.i++;
        }

    }

    public static List<String> enterVolumeData(int choice) {
        List<String> volume = new ArrayList<>();
        if (choice == 1) {
            System.out.print("Enter volume: ");
            String volumeVal = Main.scan.next();
            volume.add(volumeVal);
        } else if (choice == 2) {
            System.out.print("Enter width, height, length (include comma): ");
            List<String> volumeParams = List.of(Main.scan.next().split(","));
            volume.addAll(volumeParams);
        }
        return volume;
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

    public static int getI() {
        return i;
    }

    public static LocalDate getCurrDate() {
        return currDate;
    }

    public static List<Place> getPlaces() {
        return places;
    }

    public static List<Person> getPersons() {
        return persons;
    }

    public static Scanner getScan() {
        return scan;
    }

    public static MultiThread getMultiThread() {
        return multiThread;
    }
}
