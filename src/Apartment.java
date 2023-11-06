import java.time.LocalDate;

public class Apartment extends Place {
    private Person personPayRent;

    public Apartment(String name, int height, int width, int length) {
        super(name, height, width, length);
    }
    public Apartment(String name, int volume) {
        super(name, volume);
    }

    public static void addApartTenant(Apartment selPlace) {
        System.out.print("Tenant:\n1 - create new ");
        if (Person.allExistingPersonsSize() > 0)
            System.out.println("\n2 - add existing");
        int choice = Main.getScan().nextInt();
        Person regPerson;
        if (choice == 1) {
            regPerson = Person.createPerson();
            Person.addPersonToExisting(regPerson);
            selPlace.tenantConfig(regPerson, selPlace);

        } else if (choice == 2 && Person.allExistingPersonsSize() > 0) {
            regPerson = Person.findExistingPerson();
            if (regPerson != null)
                selPlace.tenantConfig(regPerson, selPlace);
        }
    }

    public static Apartment selApart(Person selTenant) {
        if (selTenant.rentedPlacesSize() > 0) {
            System.out.println("Select apartment: ");
            for (int i = 0; i < selTenant.rentedPlacesSize(); i++)
                if (selTenant.getRentedPlace(i) instanceof Apartment)
                    System.out.println(i + " - " + selTenant.getRentedPlace(i).getName());
            int choice = Main.getScan().nextInt();

            return (Apartment) selTenant.getRentedPlace(choice);
        }
        System.out.println("You do not rent any apartment");
        return null;
    }

    public void setPersonPayRent(Person person) {
        this.personPayRent = person;
    }

    public void addPersonPayRent(Person tenant) {
        this.personPayRent = tenant;
    }

    public String checkPersonPayRent() {
        if (this.personPayRent == null)
            return "none";
        else
            return String.valueOf(this.personPayRent);
    }

}
