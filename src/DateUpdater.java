import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class DateUpdater extends Thread {
    private static int i = 0;
    private static LocalDate currDate = LocalDate.now();

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
                DateUpdater.currDate = DateUpdater.incrementDate();
                if (DateUpdater.getI() % 2 == 0)
                    Place.checkRentedPlacesValidity();
            } catch (InterruptedException e) {
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static LocalDate incrementDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(String.valueOf(getCurrDate())));
        c.add(Calendar.DATE, 1);
        DateUpdater.incrementI();
        return LocalDate.parse(sdf.format(c.getTime()));
    }

    public static LocalDate getCurrDate() {
        return DateUpdater.currDate;
    }

    public static int getI() {
        return DateUpdater.i;
    }

    public static void incrementI() {
        DateUpdater.i++;
    }

}
