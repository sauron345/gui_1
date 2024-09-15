import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Developer {
    private final String name;
    private List<Estate> estates = new ArrayList<>();

    public Developer(String name) {
        this.name = name;
    }

    public void addEstate(Estate estate) {
        Estate.addEstateToExisting(estate);
        this.estates.add(estate);
    }

    public void addEstate(List<Estate> estates) {
        Estate.addEstateToExisting(estates);
        this.estates.addAll(estates);
    }

    public void removeEstate(Estate estate) {
        Estate.allExistingEstates.remove(estate);
        Estate.removeEstateToExisting(estate);
    }

    public void readyComponents() {
        Estate estate = Estate.readyEstate();
        this.addEstate(estate);

        Block block = Block.readyBlock();
        estate.addBlock(block);

        Person.readyPersons();
        Apartment.readyAparts(block);
        Parking.readyParkings(block);
    }

    public String showEstates() {
        String estatesList = "";
        for (int i = 0; i < this.estatesSize(); i++)
            estatesList += i + " - " + this.getEstate(i).getName() + "\n";
        return estatesList;
    }

    public Estate getEstate(int index) {
        return this.estates.get(index);
    }

    public int estatesSize() {
        return this.estates.size();
    }

}
