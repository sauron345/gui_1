import java.time.LocalDate;

public class TenantLetter {
    private final String personName;
    private final LocalDate receivedDate;
    private final Place place;

    public TenantLetter(String tenantName, Place place) {
        this.receivedDate = DateUpdater.getCurrDate();
        this.personName = tenantName;
        this.place = place;
    }

    public String read() {
        return "(Received: " + receivedDate + ")" + " Dear " + personName + ", " +
            "We would like to inform you that the rental of apartment " + place.getName() +
            " has been terminated. Please leave the apartment or extend the contract.";
    }

    public String getPersonName() {
        return personName;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public Place getPlace() {
        return place;
    }
}
