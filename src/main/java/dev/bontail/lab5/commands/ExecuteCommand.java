package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.Invoker;
import main.java.dev.bontail.lab5.Main;
import main.java.dev.bontail.lab5.PersonManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class ExecuteCommand implements Command {
    PersonManager personManager;
    Invoker invoker;

    public ExecuteCommand(PersonManager personManager, Invoker invoker) {
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
            HashSet<String> openedFiles = this.invoker.getOpenedFiles();
            if (openedFiles.contains(filename)) {
                System.out.println("Recursive execution");
                return false;
            }
            openedFiles.add(filename);
            File file = new File(filename);
            Invoker invoker = new Invoker(new Scanner(file), openedFiles);
            invoker.setCommands(Main.createCommands(this.personManager, invoker));
            invoker.startScanningConsole();
            openedFiles.remove(filename);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid filename " + e.getMessage() + " " + filename);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "execute";
    }

    @Override
    public String getHelpInfo() {
        return "Execute commands from file";
    }
}
