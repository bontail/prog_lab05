package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.Invoker;
import main.java.dev.bontail.lab5.PersonManager;

public class SaveCommand implements Command {
    PersonManager personManager;
    Invoker invoker;

    public SaveCommand(PersonManager personManager, Invoker invoker) {
        this.personManager = personManager;
        this.invoker = invoker;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 0) {
            System.out.println("save don't need args");
            return false;
        }

        this.personManager.write();
        System.out.println("save done");
        return true;
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getHelpInfo() {
        return "Save collection to file";
    }
}
