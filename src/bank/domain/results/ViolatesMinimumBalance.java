package bank.domain.results;

import bank.domain.valueobjects.Money;

public class ViolatesMinimumBalance implements OperationResult {
    private final Money minimum;
    private final Money resultingBalance;
    public ViolatesMinimumBalance(Money minimum, Money resultingBalance) { 
        this.minimum = minimum; 
        this.resultingBalance = resultingBalance; 
    }
    public Money getMinimum() { return minimum; }
    public Money getResultingBalance() { return resultingBalance; }
}