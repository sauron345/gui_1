public class Apartment extends Place {
    private Person personPayRent;

    public Apartment(String name, int height, int width, int length) {
        super(name, height, width, length);
    }
    public Apartment(String name, int volume) {
        super(name, volume);
    }

    private void assignRental(Person tenant) {
        this.personPayRent = tenant;
        super.addPersonToPlace(tenant);
    }

    public static void addApartTenant(Apartment selPlace) {
        System.out.print("Tenant:\n1 - create new ");
        if (Person.getAllExistingPersons().size() > 0)
            System.out.println("\n2 - add existing");
        int choice = Main.getScan().nextInt();
        Person regPerson;
        if (choice == 1) {
            regPerson = Person.createPerson();
            regPerson.setPersonAsTenant();
            Person.addPersonToExisting(regPerson);
            selPlace.assignRental(regPerson);
        } else if (choice == 2 && Person.getAllExistingPersons().size() > 0) {
            regPerson = Person.findExistingPerson();
            if (regPerson != null) {
                regPerson.setPersonAsTenant();
                selPlace.assignRental(regPerson);
            }
        }
    }

    public void setPersonPayRent(Person person) {
        this.personPayRent = person;
    }

    public static Apartment selApart(Person selTenant) {
        System.out.print("Select apartment: ");
        for (int i = 0; i < selTenant.getRentedPlaces().size(); i++)
            if (selTenant.getRentedPlaces().get(i) instanceof Apartment)
                System.out.println(i + " - " + selTenant.getRentedPlaces().get(i));
        int choice = Main.getScan().nextInt();

        return (Apartment) selTenant.getRentedPlaces().get(choice);
    }

    public Person getPersonPayRent() {
        return personPayRent;
    }

}
