package bank.domain.results;

import bank.domain.valueobjects.Money;

public class InsufficientFunds implements OperationResult {
    private final Money available;
    private final Money requested;
    public InsufficientFunds(Money available, Money requested) { 
        this.available = available; 
        this.requested = requested; 
    }
    public Money getAvailable() { return available; }
    public Money getRequested() { return requested; }
}