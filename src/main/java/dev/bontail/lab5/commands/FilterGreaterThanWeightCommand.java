package main.java.dev.bontail.lab5.commands;

import main.java.dev.bontail.lab5.data.Person;
import main.java.dev.bontail.lab5.PersonManager;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FilterGreaterThanWeightCommand implements Command {
    PersonManager personManager;

    public FilterGreaterThanWeightCommand(PersonManager personManager) {
        this.personManager = personManager;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 1) {
            System.out.println("filter_greater_than_weight need one args");
            return false;
        }

        float weight;
        try {
            weight = Float.parseFloat(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        ArrayList<Person> persons = this.personManager.getFilteredPersons(
                person -> person.getWeight() != null && person.getWeight() > weight
        ).collect(Collectors.toCollection(ArrayList::new));
        for (Person person: persons){
            System.out.print(person + ", ");
        }
        System.out.println();
        return true;
    }

    @Override
    public String getName() {
        return "filter_greater_than_weight";
    }

    @Override
    public String getHelpInfo() {
        return "Print persons that weight greater than the given weight";
    }
}
