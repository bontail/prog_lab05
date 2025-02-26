package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.Invoker;
import main.java.dev.bontail.lab5.PersonManager;
import main.java.dev.bontail.lab5.validator.Validator;
import main.java.dev.bontail.lab5.data.Person;

import java.util.ArrayList;
import java.util.Collections;

public class RemoveLowerCommand extends AddCommand {
    PersonManager personManager;
    Invoker invoker;

    public RemoveLowerCommand(PersonManager personManager, Invoker invoker) {
        super(personManager, invoker);
        this.personManager = personManager;
        this.invoker = invoker;
    }

    @Override
    public Boolean execute(String[] args) {
        Validator personValidator = new Validator(Person.class, false);
        if (args.length != personValidator.getCheckedFields().size()) {
            System.out.println("remove_lower need " + personValidator.getCheckedFields().size() + " fields [name, height, weight, hairColor, nationality]");
            return false;
        }

        Person person = this.createPerson(args);
        ArrayList<Person> persons = this.personManager.getPersons();
        persons.add(person);
        Collections.sort(persons);

        System.out.println("Remove persons:");
        for (Person person1 : persons) {
            if (person.equals(person1)) {
                break;
            }
            System.out.println(person1);
            this.personManager.removePerson(person1);
        }

        return true;
    }

    @Override
    public String getName() {
        return "remove_lower";
    }

    @Override
    public String getHelpInfo() {
        return "Remove all persons from collection that are less than new person";
    }
}
