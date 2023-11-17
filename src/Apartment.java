import java.util.Arrays;

public class Apartment extends Place {
    private Person personPayRent;

    public Apartment(String name, int volume) {
        super(name, volume);
    }

    public Apartment(String name, int height, int width, int length) {
        super(name, height, width, length);
    }

    @Override
    public void selectedPlaceDetails() {
        this.showPersonsLiving();
        String choice = Main.getScan().next();
        this.selPersonLiving(choice);
    }

    public void selPersonLiving(String choice) {
        Person selPerson = null;
        if (choice.equals("d"))
            this.showPlaceDetails();
        else if (choice.equals("t"))
            selPerson = this.tenant;
        else if (this.checkLivingPersonsIsEmpty() && choice.equals("a"))
            this.addApartTenant();
        else
            selPerson = this.getPersonWithoutTenant(Integer.parseInt(choice));

        if (selPerson != null)
            selPerson.selPersonActions(this);
    }

    private Person getPersonWithoutTenant(int choice) {
        choice -= 1;
        return this.getLivingPerson(choice);
    }

    @Override
    public String showPlaceContent() {
        String livingPersonsList = "| ";
        if (this.livingPersonsSize() > 0)
            for (int i = 0; i < this.livingPersonsSize(); i++)
                livingPersonsList += this.getLivingPerson(i).getName() + " | ";
        else
            return "nothing inside yet";

        return livingPersonsList.trim();
    }

    private void displayLivingPersons() {
        System.out.println("Living persons:");
        for (int i = 0; i < this.livingPersonsSize(); i++)
            System.out.println(i + 1  + " - " + this.getLivingPerson(i).getName());
    }

    public void showPersonsLiving() {
        System.out.println("\nSelect person living in " + this.getName() + ":");
        System.out.println("d - Apartment details");
        if (this.checkLivingPersonsIsEmpty() && this.tenant == null) {
            System.out.println("No one");
            System.out.println("a - Add tenant");
        } else {
            System.out.println("------------------");
            System.out.println("t - (Tenant) " + this.tenant.getName());
            System.out.println("Living persons:");
            for (int i = 0; i < this.livingPersonsSize(); i++)
                System.out.println(i + " - " + this.getLivingPerson(i).getName());
        }
    }

    public void addApartTenant() {
        System.out.print("Tenant:\n1 - create new ");
        if (Person.allExistingPersonsSize() > 0)
            System.out.println("\n2 - add existing");

        int choice = Main.getScan().nextInt();
        Person tenant;

        if (choice == 1) {
            tenant = Person.createPerson();
            Person.addPersonToExisting(tenant);
            this.rentalConfig(tenant);
        } else if (choice == 2 && Person.allExistingPersonsSize() > 0) {
            tenant = Person.findExistingPerson();
            if (tenant != null)
                this.foundedTenant(tenant);
        }
    }

    private void foundedTenant(Person tenant) {
        System.out.println("Person is found");
        this.rentalConfig(tenant);
    }

    public static void readyAparts(Block block) {
        Apartment apartment = new Apartment("room1", 42);
        Apartment apartment2 = new Apartment("room2", 54);
        Apartment apartment3 = new Apartment("room3", 49);
        Apartment apartment4 = new Apartment("room4", 66);
        Apartment apartment5 = new Apartment("room5", 54);
        Apartment apartment6 = new Apartment("room6",56);
        block.assignPlace(Arrays.asList(apartment, apartment2, apartment3, apartment4, apartment5, apartment6));
    }

    @Override
    protected void clearPlace() {
        this.rentEnd = null;
        this.rentStart = null;
        this.clearRentEnd();
        this.clearLivingPersons();
        this.removePersonPayRent();
        this.tenant.removeRentedPlace(this);
        this.removeTenant();
    }

    @Override
    public void showPlaceDetails() {
        this.displayBaseDetails();
        System.out.println("Person pay rent: " + this.checkPersonPayRent());
        if (this.livingOrdinaryPersons.isEmpty())
            System.out.println("Living persons: none");
        else
            this.displayLivingPersons();
    }

    @Override
    public void showPlaceActions() {
        System.out.println("1 - register person");
        System.out.println("2 - checkout person");
        System.out.println("3 - cancel rental");
        System.out.println("4 - renew rental");
        System.out.println("5 - about the person");
    }

    @Override
    public void placeActions(Person selTenant, int choice) {
        switch (choice) {
            case 1 -> selTenant.regPerson(this);
            case 2 -> selTenant.checkOutPerson(this);
            case 3 -> selTenant.cancelRental(this);
            case 4 -> selTenant.renewRental(this);
            case 5 -> selTenant.showPersonDetails();
            default -> System.out.println("Entered digit does not have an action");
        }
    }

    public Person selectPerson() {
        if (this.livingPersonsSize() > 0) {
            System.out.println("Living persons in " + this.getName());
            for (int i = 0; i < this.livingPersonsSize(); i++)
                System.out.println(i + " - " + this.getLivingPerson(i).getName());
            int choice = Main.getScan().nextInt();

            return this.getLivingPerson(choice);
        }
        return null;
    }

    public String checkPersonPayRent() {
        if (this.personPayRent != null)
            return this.personPayRent.getName();
        else
            return "none";
    }

    public void setPersonPayRent(Person tenant) {
        this.personPayRent = tenant;
    }

    public void removePersonPayRent() {
        this.personPayRent = null;
    }

}
