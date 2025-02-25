package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.Invoker;

public class ExitCommand implements Command {
    Invoker invoker;

    public ExitCommand(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 0) {
            System.out.println("exit don't need args");
            return false;
        }

        this.invoker.stopApp();
        return true;
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getHelpInfo() {
        return "Exit app without saving";
    }
}
