import java.time.LocalDate;

public class TenantLetter {
    private String name;
    private LocalDate receivedDate;
    private Place place;

    public TenantLetter(String tenantName, Place place) {
        this.receivedDate = Main.getCurrDate();
        this.name = tenantName;
        this.place = place;
    }

    public String read() {
        return "Dear " + name + ", " +
            "We would like to inform you that the rental of apartment " + place.getName() +
            " has been terminated. Please leave the apartment or extend the contract" +
            "\nReceived: " + receivedDate;
    }

    public String getName() {
        return name;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public Place getPlace() {
        return place;
    }
}
