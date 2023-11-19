public class ProblematicTenantException extends RuntimeException {
    public ProblematicTenantException(Person person) {
        super("Person " + person.getName() + " already has rented places: " + person.getRentedPlaces());
    }
}
