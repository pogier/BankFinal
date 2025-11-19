package bank.domain.events;

import bank.domain.valueobjects.AccountNumber;
import bank.domain.valueobjects.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public class FundsDepositedEvent implements DomainEvent {
    private final UUID eventId;
    private final LocalDateTime occurredOn;
    private final AccountNumber accountNumber;
    private final Money amount;
    private final Money oldBalance;
    private final Money newBalance;

    public FundsDepositedEvent(AccountNumber accountNumber, Money amount, Money oldBalance, Money newBalance) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
    }

    @Override public UUID getEventId() { return eventId; }
    @Override public LocalDateTime getOccurredOn() { return occurredOn; }
    @Override public String getEventType() { return "FUNDS_DEPOSITED"; }
    
    public AccountNumber getAccountNumber() { return accountNumber; }
    public Money getAmount() { return amount; }
    public Money getOldBalance() { return oldBalance; }
    public Money getNewBalance() { return newBalance; }
}