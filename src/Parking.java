import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Parking extends Place {
    public List<Thing> storedThings = new ArrayList<>();
    public int availableSpace;

    public Parking(String name, int volume) {
        super(name, volume);
        this.availableSpace = volume;
    }

    public Parking(String name, int height, int width, int length) {
        super(name, height, width, length);
        this.availableSpace = volume;
    }

    public void storeThing(Thing thing) throws TooManyThingsException {
        this.availableSpace = currFreeSpace();
        if (thing.area <= this.availableSpace)
            this.storedThings.add(thing);
        else
            throw new TooManyThingsException();
    }

    private int currFreeSpace() {
        if (this.storedThings.size() > 0) {
            for (Thing storeThing : this.storedThings)
                this.availableSpace -= storeThing.area;
        } else
            this.availableSpace = this.volume;
        return this.availableSpace;
    }

    public void clearParking() {
        for (Thing storedThing : this.storedThings) {
            if (storedThing instanceof Vehicle)
                Vehicle.soldVehicles.add(storedThing);
            else
                Thing.thingsToUtilization.add(storedThing);
            this.storedThings.remove(storedThing);
        }
    }


}
