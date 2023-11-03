import java.util.ArrayList;
import java.util.List;

public class Developer {
    private String name;
    private List<Estate> estates = new ArrayList<>();

    public Developer(String name) {
        this.name = name;
    }

    public void addEstate(Estate estate) {
        this.estates.add(estate);
        Estate.addEstateToExisting(estate);
    }
    public void addEstate(List<Estate> estates) {
        this.estates.addAll(estates);
        Estate.addEstateToExisting(estates);
    }

    public void removeEstate(Estate estate) {
        this.estates.remove(estate);
        Estate.removeEstateToExisting(estate);
    }

    public String getName() {
        return name;
    }

    public List<Estate> getEstates() {
        return estates;
    }

}
