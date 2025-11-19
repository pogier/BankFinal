package bank.presentation.console;

import java.util.HashMap;
import java.util.Map;

public abstract class ConsoleMenu {
    protected final ConsoleInputHandler inputHandler;
    protected final Map<Integer, MenuOption> options;

    protected ConsoleMenu(ConsoleInputHandler inputHandler) {
        this.inputHandler = inputHandler;
        this.options = new HashMap<>();
        initializeOptions();
    }

    protected abstract void initializeOptions();

    public void display() {
        while (true) {
            displayHeader();
            displayOptions();
            
            int choice = inputHandler.readInt("Enter your choice: ", 
                c -> options.containsKey(c) || c == 0);
            
            if (choice == 0) {
                break;
            }
            
            MenuOption option = options.get(choice);
            if (option != null) {
                option.execute();
            }
        }
    }

    protected abstract void displayHeader();

    private void displayOptions() {
        options.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> 
                    System.out.printf("%d. %s%n", entry.getKey(), entry.getValue().getDescription()));
        System.out.println("0. Back");
    }

    protected record MenuOption(String description, Runnable action) {
        public void execute() {
            action.run();
        }
        
        public String getDescription() {
            return description;
        }
    }
}