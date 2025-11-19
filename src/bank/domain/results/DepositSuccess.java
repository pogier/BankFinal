package bank.domain.results;

import bank.domain.valueobjects.Money;

public class DepositSuccess implements OperationResult {
    private final Money newBalance;
    public DepositSuccess(Money newBalance) { this.newBalance = newBalance; }
    public Money getNewBalance() { return newBalance; }
}