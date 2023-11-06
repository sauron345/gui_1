import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Estate {
    private String name;
    private List<Place> places = new ArrayList<>();
    private static List<Estate> allExistingEstates = new ArrayList<>();

    public Estate(String name) {
        this.name = name;
    }

    public Place getPlace(int index) {
        return this.places.get(index);
    }

    public int placesSize() {
        return this.places.size();
    }

    public static int allExistingEstatesSize() {
        return Estate.allExistingEstates.size();
    }

    public Place addPlace(Place place) {
        this.places.add(place);
        Place.addPlaceToExisting(place);
        return place;
    }

    public void addPlace(List<Place> places) {
        this.places.addAll(places);
        Place.addPlaceToExisting(places);
    }

    public static void addEstateToExisting(Estate estate) {
        Estate.allExistingEstates.add(estate);
    }

    public static void addEstateToExisting(List<Estate> estates) {
        Estate.allExistingEstates.addAll(estates);
    }

    public static void removeEstateToExisting(Estate estate) {
        Estate.allExistingEstates.remove(estate);
    }

    public void removePlace(Place place) {
        this.places.remove(place);
    }

    public static void showEstateOptions(Developer dev) {
        System.out.println("\nSelect estate:");
        System.out.println("20 - add estate");
        if (Estate.allExistingEstates.size() > 0) {
            System.out.println("21 - remove estate");
            System.out.println("------------------");
            System.out.println("Existing estates:");
            for (int i = 0; i < dev.estatesSize(); i++)
                System.out.println(i + " - " + dev.getEstate(i).name);
        } else
            System.out.println("No places available yet");

    }

    public static Estate selEstateOption(int choice, Developer dev) {
        Estate selEstate;
        if (choice == 20) {
            selEstate = Estate.createEstate();
            dev.addEstate(selEstate);
            return selEstate;
        } else if (choice == 21 && Estate.allExistingEstates.size() > 0) {
            selEstate = Estate.findExistingEstate();
            if (selEstate != null)
                dev.removeEstate(selEstate);
        } else
            return dev.getEstate(choice);
        return selEstate;
    }

    private static Estate findExistingEstate() {
        List<String> estateData = Estate.enterEstateName();
        for (Estate estate : Estate.allExistingEstates)
            if (estate.name.equals(estateData.get(0)))
                return estate;
        System.out.println("Entered estate is not exists");
        return null;
    }

    private static Estate createEstate() {
        List<String> placeData = Estate.enterEstateName();
        return new Estate(placeData.get(0));
    }

    private static List<String> enterEstateName() {
        System.out.print("Enter estate name: ");
        String name = Main.getScan().next();

        return Arrays.asList(name);
    }

    public String getName() {
        return name;
    }

}
