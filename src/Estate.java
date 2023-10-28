import java.util.ArrayList;
import java.util.List;

public class Estate {
    String name;
    List<Place> places = new ArrayList<>();

    public Estate(String name) {
        this.name = name;
    }

    public Place addPlace(Place place) {
        this.places.add(place);
        return place;
    }

    public void addPlace(List<Place> places) {
        this.places.addAll(places);
    }

}
