import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class StoreData {
    private String name;
    private PrintWriter save;
    private Person person;

    public StoreData(String name) throws FileNotFoundException {
        this.name = name;
        this.save = new PrintWriter(name);
    }

    public void saveToFileCurrContent() {
        for (int i = 0; i < Person.allExistingPersonsSize(); i++) {
            this.person = Person.getExistingPerson(i);
            this.save.println("id: " + person.getId());
            this.save.println("name: " + person.getName());
            this.save.println("pesel: " + person.getPesel());
            this.save.println("address: " + person.getAddress());
            this.save.println("is tenant: " + person.isTenant());
            person.passRentedPlacesToFile(save);
            this.save.println("birthday: " + person.getBirthday());
            person.displayLettersToFile(save);
            this.save.println("-------------------------------------");
        }
        this.save.close();
    }

}
