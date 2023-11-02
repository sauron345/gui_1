public class Apartment extends Place {
    public Person personPayRent;

    public Apartment(String name, int height, int width, int length) {
        super(name, height, width, length);
    }
    public Apartment(String name, int volume) {
        super(name, volume);
    }

    private void assignRental(Person tenant) {
        this.personPayRent = tenant;
        super.livingPersons.add(tenant);
    }

    public static void addApartTenant(Apartment selPlace) {
        System.out.print("Tenant:\n1 - create new ");
        if (Person.allExistingPersons.size() > 0)
            System.out.println("\n2 - add existing");
        int choice = Main.scan.nextInt();
        Person regPerson;
        if (choice == 1) {
            regPerson = Person.createPerson();
            regPerson.isTenant = true;
            Person.allExistingPersons.add(regPerson);
            selPlace.assignRental(regPerson);
        } else if (choice == 2 && Person.allExistingPersons.size() > 0) {
            regPerson = Person.findExistingPerson();
            if (regPerson != null) {
                regPerson.isTenant = true;
                selPlace.assignRental(regPerson);
            }
        }
    }

    public static Apartment selApart(Person selTenant) {
        System.out.print("Select apartment: ");
        for (int i = 0; i < selTenant.rentedPlaces.size(); i++)
            if (selTenant.rentedPlaces.get(i) instanceof Apartment)
                System.out.println(i + " - " + selTenant.rentedPlaces.get(i));
        int choice = Main.scan.nextInt();

        return (Apartment) selTenant.rentedPlaces.get(choice);
    }

    public Person getPersonPayRent() {
        return personPayRent;
    }

}
