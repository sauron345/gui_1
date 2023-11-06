public class ProblematicTenantException extends RuntimeException {
    public ProblematicTenantException(String personName) {
        super("Person " + personName + " already had 5 rooms rent");
    }
}
