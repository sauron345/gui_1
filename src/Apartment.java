public class Apartment extends Place {
    public Person personPayRent;

    public Apartment(String name, int height, int width, int length) {
        super(name, height, width, length);
    }
    public Apartment(String name, int volume) {
        super(name, volume);
    }

    public void assignRental(Person tenant) {
        this.personPayRent = tenant;
        super.livingPersons.add(tenant);
    }

}
