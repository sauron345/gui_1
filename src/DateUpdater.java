import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class DateUpdater extends Thread{

    @Override
    public void run() {
        try {
            System.out.println();
            System.out.println(Main.getCurrDate());
            Thread.sleep(5000);
            LocalDate currDate = DateUpdater.incrementDate();
            if (Main.getI() % 2 == 0)
                Place.checkRentedPlacesValidity();
        } catch (InterruptedException e ) {
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static LocalDate incrementDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(String.valueOf(Main.getCurrDate())));
        c.add(Calendar.DATE, 1);
        return LocalDate.parse(sdf.format(c.getTime()));
    }

}

