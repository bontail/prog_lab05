package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.PersonManager;

public class CountByHeightCommand implements Command {
    PersonManager personManager;

    public CountByHeightCommand(PersonManager personManager) {
        this.personManager = personManager;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 1) {
            System.out.println("count_by_height need args");
            return false;
        }


        long height;
        try {
            height = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("arg is not a number");
            return false;
        }

        long count = this.personManager.getFilteredPersons(
                person -> person.getHeight() != null && person.getHeight().equals(height)
        ).count();
        System.out.println("Count: " + count);
        return true;
    }

    @Override
    public String getName() {
        return "count_by_height";
    }

    @Override
    public String getHelpInfo() {
        return "Count persons by height";
    }
}
