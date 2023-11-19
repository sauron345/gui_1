import java.time.LocalDate;

public class TenantLetter {
    private final String personName;
    private final LocalDate receivedDate;
    private final String placeName;

    public TenantLetter(String tenantName, String placeName) {
        this.receivedDate = DateUpdater.getCurrDate();
        this.personName = tenantName;
        this.placeName = placeName;
    }

    public String read() {
        return "(Received: " + receivedDate + ")" + " Dear " + personName + ", " +
            "We would like to inform you that the rental of apartment " + placeName +
            " has been terminated. Please leave the apartment or extend the contract.";
    }

    public String getPersonName() {
        return personName;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public String getPlaceName() {
        return placeName;
    }

}
