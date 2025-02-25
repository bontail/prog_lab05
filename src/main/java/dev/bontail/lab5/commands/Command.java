package main.java.dev.bontail.lab5.commands;

public interface Command {
    Boolean execute(String[] args);
    String getName();
    String getHelpInfo();
}
