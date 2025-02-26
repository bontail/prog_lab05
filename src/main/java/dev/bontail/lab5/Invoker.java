package main.java.dev.bontail.lab5;

import lombok.Getter;
import main.java.dev.bontail.lab5.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Save all commands
 * Save history
 * Works with console
 */
public class Invoker {
    private final static int HISTORY_SIZE = 11;
    LinkedHashMap<String, Command> commands = new LinkedHashMap<>();
    private boolean isRunningApp = true;
    ArrayDeque<Command> history = new ArrayDeque<>(HISTORY_SIZE);
    Scanner sc;

    @Getter
    HashSet<String> openedExecutionScripts;


    public Invoker(Scanner sc, HashSet<String> openedExecutionScripts) {
        this.sc = sc;
        this.sc.useDelimiter("\n");
        this.openedExecutionScripts = openedExecutionScripts;
    }

    public ArrayList<Command> getCommands() {
        return new ArrayList<>(this.commands.values());
    }

    public void setCommands(@NotNull ArrayList<Command> commands) {
        for (Command command : commands) {
            this.commands.put(command.getName(), command);
        }
    }

    public void stopApp() {
        this.isRunningApp = false;
    }

    private void addCommandToHistory(@NotNull Command command) {
        if (this.history.size() == HISTORY_SIZE) {
            this.history.removeFirst();
        }
        this.history.push(command);
    }

    public Collection<Command> getHistory() {
        return this.history;
    }

    public String[] getLineTokens() {
        String line = sc.next();
        String[] tokens = line.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("?")) {
                tokens[i] = null;
            }
        }
        return tokens;
    }

    public boolean askForBackup(){
        System.out.println("You didn't save your changes and want to go back to them? (Yes/No)");
        String[] tokens = this.getLineTokens();
        while (tokens.length != 1 || (!tokens[0].equals("Yes") && !tokens[0].equals("No"))){
            System.out.println("Please enter Yes or No");
            tokens = this.getLineTokens();
        }
        return tokens[0].equals("Yes");
    }

    public void startScanningConsole() {
        while (this.isRunningApp && sc.hasNext()) {
            String[] tokens = this.getLineTokens();
            if (tokens.length == 1 && tokens[0].isEmpty()) {
                continue;
            }

            Command command = this.commands.get(tokens[0]);
            if (command == null) {
                System.out.println("Unknown command: " + tokens[0]);
                continue;
            }

            String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (command.execute(args)) {
                this.addCommandToHistory(command);
            }
        }
    }
}
