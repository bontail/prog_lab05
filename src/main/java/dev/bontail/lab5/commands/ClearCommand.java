package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.PersonManager;

public class ClearCommand implements Command {
    PersonManager personManager;

    public ClearCommand(PersonManager personManager) {
        this.personManager = personManager;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 0) {
            System.out.println("clear don't need args");
            return false;
        }

        this.personManager.clear();
        System.out.println("Successful clear");
        return true;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelpInfo() {
        return "Clears all persons";
    }
}
