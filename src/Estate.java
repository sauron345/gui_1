import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Estate {
    public String name;
    public List<Place> places = new ArrayList<>();
    public static List<Estate> allExistingEstates = new ArrayList<>();

    public Estate(String name) {
        this.name = name;
    }

    public Place addPlace(Place place) {
        this.places.add(place);
        Place.allExistingPlaces.add(place);
        return place;
    }

    public void addPlace(List<Place> places) {
        this.places.addAll(places);
        Place.allExistingPlaces.addAll(places);
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
            for (int i = 0; i < dev.getEstates().size(); i++)
                System.out.println(i + " - " + dev.getEstates().get(i).name);
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
            return dev.getEstates().get(choice);
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
        String name = Main.scan.next();

        return Arrays.asList(name);
    }

    public String getName() {
        return name;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public static List<Estate> getAllExistingEstates() {
        return allExistingEstates;
    }
}
