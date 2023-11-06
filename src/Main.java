import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

class Main extends DateUpdater {
    private static int i = 1;
    private static LocalDate currDate;
    private static List<Place> places;
    private static List<Person> persons;
    private static final Scanner scan = new Scanner(System.in);
    private static final DateUpdater multiThread = new DateUpdater();
    private static List<Developer> devs = new ArrayList<>();

    public static void main(String[] args) throws TooManyThingsException, FileNotFoundException {
        Main main = new Main();

        Main.currDate = LocalDate.now();
        int choice;

        StoreData storedDataFile = new StoreData("personsData.txt");
        Developer dev = new Developer("Patryk");
        main.readyComponents(dev);

        while (true) {
            Main.multiThread.run();
            Estate.showEstateOptions(dev);
            System.out.println("-------------------------------------");
            System.out.println("30 - Store current persons data to the file");

            choice = scan.nextInt();
            Estate selEstate;

            if (choice == 30)
                storedDataFile.saveToFileCurrContent();
            else {
                selEstate = Estate.selEstateOption(choice, dev);

                Place.showPlaceOptions(selEstate);
                choice = scan.nextInt();
                Place selPlace = Place.selPlaceOption(choice, selEstate);

                if (selPlace instanceof Apartment) {
                    Person.showPersonsLiving(selPlace);
                    choice = scan.nextInt();
                    Person.selPersonLiving(choice, selPlace);
                } else if (selPlace instanceof Parking) {
                    Parking.showStoredThings((Parking) selPlace);
                    choice = scan.nextInt();
                    Thing.selThingOption(choice, (Parking) selPlace);
                }
            }
            Main.i++;
        }
    }

    private static void readyPersons() {
        Person person = new Person("Sebastian", "Wieczorek", "1236432212", "2002-11-30", "Przasnyska 23a");
        Person person2 = new Person("Damian", "Mołdawski", "423423234", "2002-09-23", "Kwiatkowskiego 1c");
        Person person3 = new Person("Robert", "Kowalski", "1231232132", "2005-05-15", "Bogusławskiego 3");
        Person person4 = new Person("Lidia", "Szczypior", "543342158", "2002-12-11", "Powązkowska 25b");
        Person person5 = new Person("Piotr", "Wróbel", "1247964332", "2003-07-21", "Maczka 43");

        persons = List.of(person, person2, person3, person4, person5);
        Person.addPersonToExisting(persons);
    }

    private static List<Apartment> readyAparts() {
        List<Apartment> tempArr = new ArrayList<>();
        tempArr.add(new Apartment("room1", 42));
        tempArr.add(new Apartment("room2", 54));
        tempArr.add(new Apartment("room3", 49));
        tempArr.add(new Apartment("room4", 66));
        tempArr.add(new Apartment("room5", 54));
        tempArr.add(new Apartment("room6",56));
        return tempArr;
    }

    private static List<Parking> readyParkings() {
        List<Parking> tempArr = new ArrayList<>();
        tempArr.add(new Parking("parking1", 30));
        tempArr.add(new Parking("parking2", 54));
        tempArr.add(new Parking("parking3", 24));
        tempArr.add(new Parking("parking4", 32));
        return tempArr;
    }

    private Developer readyComponents(Developer dev) {
        Estate estate = new Estate("Żoliborz");
        dev.addEstate(estate);

        Main.readyPersons();
        estate.addPlace((Place) Main.readyAparts());
        estate.addPlace((Parking) Main.readyParkings());

        return dev;
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

    public static Developer getDevs(int index) {
        return devs.get(index);
    }

    public static int devsSize() {
        return devs.size();
    }

    public static int getI() {
        return i;
    }

    public static LocalDate getCurrDate() {
        return currDate;
    }

    public static Scanner getScan() {
        return scan;
    }

    public static DateUpdater getMultiThread() {
        return multiThread;
    }
}
