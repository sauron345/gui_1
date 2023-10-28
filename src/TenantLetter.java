import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class TenantLetter {
    public String tenantName;
    public LocalDate receivedDate;
    public Place place;

    public TenantLetter(String tenantName, Place place) {
        this.receivedDate = Main.currDate;
        this.tenantName = tenantName;
        this.place = place;
    }
    public void read() {
        System.out.println("Dear " + tenantName + ", " +
            "We would like to inform you that the rental of apartment " + place +
            " has been terminated. Please leave the apartment or extend the contract");
    }

}
