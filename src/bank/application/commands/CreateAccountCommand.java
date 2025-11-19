package bank.application.commands;

import bank.domain.valueobjects.Money;

import java.util.Currency;

public record CreateAccountCommand(
    String accountHolderName,
    Money initialDeposit,
    String accountType
) {
    public CreateAccountCommand {
        if (accountHolderName == null || accountHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty");
        }
        if (initialDeposit == null || !initialDeposit.isPositive()) {
            throw new IllegalArgumentException("Initial deposit must be positive");
        }
    }
    
    public static CreateAccountCommand of(String name, double amount, String currencyCode, String type) {
        return new CreateAccountCommand(
            name, 
            Money.of(amount, Currency.getInstance(currencyCode)), 
            type
        );
    }
}