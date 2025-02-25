package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.Invoker;
import main.java.dev.bontail.lab5.PersonManager;
import main.java.dev.bontail.lab5.ReflectionInvoker;
import main.java.dev.bontail.lab5.data.Person;

import java.util.ArrayList;
import java.util.Collections;

public class AddIfMaxCommand extends AddCommand {
    PersonManager personManager;
    Invoker invoker;

    public AddIfMaxCommand(PersonManager personManager, Invoker invoker) {
        super(personManager, invoker);
        this.personManager = personManager;
        this.invoker = invoker;
    }

    @Override
    public Boolean execute(String[] personArgs) {
        ReflectionInvoker personReflectionInvoker = new ReflectionInvoker(Person.class, false);
        if (personArgs.length != personReflectionInvoker.getCheckedFields().size()) {
            System.out.println("add_if_max need " + personReflectionInvoker.getCheckedFields().size() + " fields [name, height, weight, hairColor, nationality]");
            return false;
        }

        Person person = this.createPerson(personArgs);
        ArrayList<Person> persons = this.personManager.getPersons();
        System.out.println(persons);
        persons.add(person);
        Collections.sort(persons);
        if (persons.get(persons.size() - 1).equals(person)) {
            this.personManager.addPerson(person);
            System.out.println(person.getName() + " added");
            return true;
        }

        System.out.println(person.getName() + " don't added");
        return true;
    }

    @Override
    public String getName() {
        return "add_if_max";
    }

    @Override
    public String getHelpInfo() {
        return "Add a person to collection if new person is max";
    }
}
