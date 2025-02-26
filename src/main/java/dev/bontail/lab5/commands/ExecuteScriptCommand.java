package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.Invoker;
import main.java.dev.bontail.lab5.Main;
import main.java.dev.bontail.lab5.PersonManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class ExecuteScriptCommand implements Command {
    PersonManager personManager;
    Invoker invoker;

    public ExecuteScriptCommand(PersonManager personManager, Invoker invoker) {
        this.invoker = invoker;
        this.personManager = personManager;
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 1) {
            System.out.println("execute need filename");
            return false;
        }

        String filename = args[0];
        try {
            HashSet<String> openedExecutionScripts = this.invoker.getOpenedExecutionScripts();
            if (openedExecutionScripts.contains(filename)) {
                System.out.println("Recursive execution");
                return false;
            }
            openedExecutionScripts.add(filename);
            File file = new File(filename);
            Invoker invoker = new Invoker(new Scanner(file), openedExecutionScripts);
            invoker.setCommands(Main.createCommands(this.personManager, invoker));
            invoker.startScanningConsole();
            openedExecutionScripts.remove(filename);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid filename " + e.getMessage() + " " + filename);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getHelpInfo() {
        return "Execute commands from file";
    }
}
