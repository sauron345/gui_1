import java.util.ArrayList;
import java.util.List;

public class Developer {
    public String name;
    public List<Estate> estates = new ArrayList<>();

    public Developer(String name) {
        this.name = name;
    }
    public void addEstate(Estate estate) {
        this.estates.add(estate);
        Estate.allExistingEstates.add(estate);
    }
    public void addEstate(List<Estate> estates) {
        this.estates.addAll(estates);
        Estate.allExistingEstates.addAll(estates);
    }

    public void removeEstate(Estate estate) {
        this.estates.remove(estate);
        Estate.allExistingEstates.remove(estate);
    }

}
