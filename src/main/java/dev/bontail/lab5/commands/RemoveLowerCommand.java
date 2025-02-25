package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.Invoker;
import main.java.dev.bontail.lab5.PersonManager;
import main.java.dev.bontail.lab5.ReflectionInvoker;
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
        ReflectionInvoker personReflectionInvoker = new ReflectionInvoker(Person.class, false);
        if (args.length != personReflectionInvoker.getCheckedFields().size()) {
            System.out.println("remove_lower need " + personReflectionInvoker.getCheckedFields().size() + " fields [name, height, weight, hairColor, nationality]");
            return false;
        }

        Person person = this.createPerson(args);
        ArrayList<Person> persons = this.personManager.getPersons();
        persons.add(person);
        Collections.sort(persons);
        int lowerPersons = 0;
        for (Person person1 : persons) {
            if (person.equals(person1)) {
                break;
            }
            this.personManager.removePerson(person1);
            lowerPersons++;
        }
        this.personManager.addPerson(person);
        System.out.println("remove_lower successful delete " + lowerPersons + " persons");
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
