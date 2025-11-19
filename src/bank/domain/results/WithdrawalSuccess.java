package bank.domain.results;

import bank.domain.valueobjects.Money;

public class WithdrawalSuccess implements OperationResult {
    private final Money newBalance;
    public WithdrawalSuccess(Money newBalance) { this.newBalance = newBalance; }
    public Money getNewBalance() { return newBalance; }
}