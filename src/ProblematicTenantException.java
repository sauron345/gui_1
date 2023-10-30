import java.util.Arrays;
import java.util.List;

public class ProblematicTenantException extends RuntimeException {
    public ProblematicTenantException(String personName, List<Place> rentedPlaces) {
        super("Person " + personName + " already had rooms rent: " + rentedPlaces.toString());
    }
}
