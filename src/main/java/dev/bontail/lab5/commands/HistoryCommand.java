package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.Invoker;


public class HistoryCommand implements Command {
    Invoker invoker;

    public HistoryCommand(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 0) {
            System.out.println("history don't need args");
            return false;
        }

        int i = 1;
        for (Command command : this.invoker.getHistory()){
            System.out.println(i + ": " + command.getName());
            i++;
        }

        return true;
    }

    @Override
    public String getName() {
        return "history";
    }

    @Override
    public String getHelpInfo() {
        return "Get history successes commands";
    }
}
