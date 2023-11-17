import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class File {
    private final String name;
    private PrintWriter save;
    private Person person;

    public File(String name) {
        this.name = name;
    }

    public void saveToFileCurrContent() throws FileNotFoundException {
        this.save = new PrintWriter(name);
        for (int i = 0; i < Person.allExistingPersonsSize(); i++) {
            this.person = Person.getExistingPerson(i);
            this.save.println("id: " + person.getId());
            this.save.println("name: " + person.getName());
            this.save.println("pesel: " + person.getPesel());
            this.save.println("address: " + person.getAddress());
            this.save.println("is tenant: " + person.isTenant());
            this.save.println("birthday: " + person.getBirthday());
            this.save.println("Rented places: " + person.getRentedPlaces());
            this.save.println("Tenant letters details: " + person.displayLetters());
            this.save.println("-------------------------------------");
        }
        this.save.close();
        System.out.println("Successfully sent data");
    }

}
