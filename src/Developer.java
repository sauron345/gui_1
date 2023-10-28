import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Developer {
    List<Estate> estatesOwner = new ArrayList<>();

    public Developer(Estate estate) {
        this.estatesOwner.add(estate);
    }
    public Developer(List<Estate> estates) {
        this.estatesOwner.addAll(estates);
    }
}
