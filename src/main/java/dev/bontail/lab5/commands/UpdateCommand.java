package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.Invoker;
import main.java.dev.bontail.lab5.PersonManager;
import main.java.dev.bontail.lab5.ReflectionInvoker;
import main.java.dev.bontail.lab5.data.Person;

public class UpdateCommand extends AddCommand {
    PersonManager personManager;
    Invoker invoker;

    public UpdateCommand(PersonManager personManager, Invoker invoker) {
        super(personManager, invoker);
        this.personManager = personManager;
        this.invoker = invoker;
    }

    @Override
    public Boolean execute(String[] personArgs) {
        ReflectionInvoker personReflectionInvoker = new ReflectionInvoker(Person.class, false);
        if (personArgs.length != personReflectionInvoker.getCheckedFields().size() + 1) {
            System.out.println("update need " + personReflectionInvoker.getCheckedFields().size() + 1 + " fields");
            return false;
        }

        int id;
        try {
            id = Integer.parseInt(personArgs[0]);
        } catch (NumberFormatException e) {
            System.out.println("id is not a number");
            return false;
        }
        Person personById = this.personManager.getPersonByField(person -> person.getId() == id);
        if (personById == null) {
            System.out.println("Person not found");
            return false;
        }

        String[] newPersonArgs = new String[personArgs.length - 1];
        System.arraycopy(personArgs, 1, newPersonArgs, 0, newPersonArgs.length);

        Person person = this.createPerson(newPersonArgs);
        personById.clonePerson(person);
        System.out.println("Person updated");
        return true;
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getHelpInfo() {
        return "Update person by id";
    }
}
