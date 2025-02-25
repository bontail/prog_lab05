package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.PersonManager;

public class InfoCommand implements Command {
    private final PersonManager personManager;

    public InfoCommand(PersonManager personManager) {
        this.personManager = personManager;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 0) {
            System.out.println("info don't need args");
            return false;
        }

        System.out.println("Type: " + this.personManager.getCollectionType() + '\n' + "Creation Date: " + this.personManager.creationDate + '\n' + "Length: " + this.personManager.getLength());
        return true;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getHelpInfo() {
        return "Get information about collection";
    }
}
