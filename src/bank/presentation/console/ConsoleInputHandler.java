package bank.presentation.console;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConsoleInputHandler {
    private final Scanner scanner;

    public ConsoleInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt, Predicate<Integer> validator) {
        return readInput(prompt, Integer::parseInt, validator, "Please enter a valid integer");
    }

    public double readDouble(String prompt, Predicate<Double> validator) {
        return readInput(prompt, Double::parseDouble, validator, "Please enter a valid number");
    }

    public BigDecimal readBigDecimal(String prompt, Predicate<BigDecimal> validator) {
        return readInput(prompt, BigDecimal::new, validator, "Please enter a valid amount");
    }

    private <T> T readInput(String prompt, Function<String, T> parser, 
                           Predicate<T> validator, String errorMessage) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                T value = parser.apply(input);
                
                if (validator.test(value)) {
                    return value;
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            } catch (Exception e) {
                System.out.println(errorMessage);
            }
        }
    }
}