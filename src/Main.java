import java.io.FileNotFoundException;
import java.util.*;

class Main extends DateUpdater {
    private static final Scanner scan = new Scanner(System.in);
    private static List<Developer> devs = new ArrayList<>();
    private String choice;

    public static void main(String[] args) throws TooManyThingsException {
        Main main = new Main();
        DateUpdater dateUpdater = new DateUpdater();
        File storedDataFile = new File("personsData.txt");
        Developer dev = new Developer("Patryk");
        dev.readyComponents();

        dateUpdater.start();
        Main.loop(main, dev, storedDataFile);
    }

    private static void loop(Main main, Developer dev, File storedDataFile) {
        while (true) {
            main.showStartOptions(dev);
            main.choice = scan.next();
            try {
                main.selectedStartOption(main.choice, storedDataFile, dev);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showStartOptions(Developer dev) {
        Estate.showEstateOptions(dev);
        System.out.println("-------------------------------------");
        System.out.println("s - Store current persons data to the file");
    }

    private void selectedStartOption(String choice, File storedDataFile, Developer dev) throws FileNotFoundException {
        Estate selEstate;
        if (choice.equals("s"))
            storedDataFile.saveToFileCurrContent();
        else {
            selEstate = Estate.selEstateOption(choice, dev);
            Block.showBlockOptions(selEstate);
            choice = scan.next();
            Block selBlock = Block.selBlockOption(choice, selEstate);
            if (selBlock != null) {
                Place.showPlaceOptions(selBlock);
                choice = scan.next();
                Place selPlace = Place.selPlaceOption(choice, selBlock);
                selPlace.selectedPlaceDetails();
            } else
                System.out.println("Entered block is not exists");
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

    public static Developer getDevs(int index) {
        return devs.get(index);
    }

    public static int devsSize() {
        return devs.size();
    }

    public static Scanner getScan() {
        return scan;
    }

}
