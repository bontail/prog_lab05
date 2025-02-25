package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.Invoker;

public class HelpCommand implements Command {
    private final Invoker invoker;

    public HelpCommand(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length == 0) {
            for (Command command : invoker.getCommands()){
                System.out.println(command.getName() + ": " + command.getHelpInfo());
            }
            return true;
        }

        for (String arg : args) {
            Command command = this.invoker.getCommands().stream().
                    filter(command1 -> command1.getName().equals(arg)).findFirst().orElse(null);
            if (command == null) {
                System.out.println("Unknown command: " + arg);
                return false;
            } else {
                System.out.println(command.getName() + ": " + command.getHelpInfo());
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelpInfo() {
        return "Get information about all main.java.dev.bontail.lab5.commands";
    }
}
