import java.util.ArrayList;
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

}
