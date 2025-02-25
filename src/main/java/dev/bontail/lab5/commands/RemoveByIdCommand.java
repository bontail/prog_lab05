package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.PersonManager;
import main.java.dev.bontail.lab5.data.Person;

public class RemoveByIdCommand implements Command {
    PersonManager personManager;

    public RemoveByIdCommand(PersonManager personManager) {
        this.personManager = personManager;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 1) {
            System.out.println("remove_by_id need one arg");
            return false;
        }

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("arg is not a number");
            return false;
        }

        Person personById = personManager.getPersonByField(person -> person.getId() == id);
        if (personById == null) {
            System.out.println("person not found");
            return true;
        }
        personManager.removePerson(personById);
        System.out.println("person removed");
        return true;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getHelpInfo() {
        return "Remove element from collection";
    }
}
