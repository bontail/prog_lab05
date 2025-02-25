package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.PersonManager;

public class ShowCommand implements Command {
    PersonManager personManager;

    public ShowCommand(PersonManager personManager) {
        this.personManager = personManager;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 0) {
            System.out.println("show don't need args");
            return false;
        }

        System.out.print(personManager);
        return true;
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getHelpInfo() {
        return "Show all persons";
    }
}
