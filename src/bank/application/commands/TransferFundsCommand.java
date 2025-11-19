package bank.application.commands;

import bank.domain.valueobjects.AccountNumber;
import bank.domain.valueobjects.Money;

public record TransferFundsCommand(
    AccountNumber sourceAccountNumber,
    AccountNumber destinationAccountNumber,
    Money amount,
    String reference
) {
    public TransferFundsCommand {
        if (sourceAccountNumber.equals(destinationAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
    }
}