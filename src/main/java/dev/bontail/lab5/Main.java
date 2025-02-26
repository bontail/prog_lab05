package main.java.dev.bontail.lab5;
import main.java.dev.bontail.lab5.commands.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        PersonManager personManager = new PersonManager();
        Invoker invoker = new Invoker(new Scanner(System.in), new HashSet<>());
        ArrayList<Command> commands = Main.createCommands(personManager, invoker);
        invoker.setCommands(commands);
        if (Dumper.isActiveBackup(personManager.getPersons())){
            if (invoker.askForBackup()) {
                personManager.setPersons(Dumper.getBackupPersons());
                System.out.println("Successful backup installation");
            } else {
                Dumper.saveBackup(personManager.getPersons());
            }
        }
        invoker.startScanningConsole();
    }

    public static ArrayList<Command> createCommands(PersonManager personManager, Invoker invoker) {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new HelpCommand(invoker));
        commands.add(new InfoCommand(personManager));
        commands.add(new ShowCommand(personManager));
        commands.add(new AddCommand(personManager, invoker));
        commands.add(new UpdateCommand(personManager, invoker));
        commands.add(new RemoveByIdCommand(personManager));
        commands.add(new ClearCommand(personManager));
        commands.add(new SaveCommand(personManager, invoker));
        commands.add(new ExecuteScriptCommand(personManager, invoker));
        commands.add(new ExitCommand(invoker));
        commands.add(new AddIfMaxCommand(personManager, invoker));
        commands.add(new RemoveLowerCommand(personManager, invoker));
        commands.add(new HistoryCommand(invoker));
        commands.add(new CountByHeightCommand(personManager));
        commands.add(new FilterContainsNameCommand(personManager));
        commands.add(new FilterGreaterThanWeightCommand(personManager));
        return commands;
    }

}
