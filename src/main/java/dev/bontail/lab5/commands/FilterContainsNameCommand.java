package main.java.dev.bontail.lab5.commands;

import main.java.dev.bontail.lab5.data.Person;
import main.java.dev.bontail.lab5.PersonManager;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class FilterContainsNameCommand implements Command {
    PersonManager personManager;

    public FilterContainsNameCommand(PersonManager personManager) {
        this.personManager = personManager;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 1) {
            System.out.println("filter_contains_name need one arg");
            return false;
        }

        ArrayList<Person> persons = this.personManager.getFilteredPersons(
                person -> person.getName().contains(args[0])
        ).collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Persons: ");
        for (Person person: persons){
            System.out.print(person + ", ");
        }
        System.out.println();
        return true;
    }

    @Override
    public String getName() {
        return "filter_contains_name";
    }

    @Override
    public String getHelpInfo() {
        return "Print persons that contains the given name";
    }
}
