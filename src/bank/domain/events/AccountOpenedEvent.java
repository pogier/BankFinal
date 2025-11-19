package bank.domain.events;

import bank.domain.valueobjects.AccountNumber;
import bank.domain.valueobjects.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccountOpenedEvent implements DomainEvent {
    private final UUID eventId;
    private final LocalDateTime occurredOn;
    private final AccountNumber accountNumber;
    private final String accountHolderName;
    private final Money initialBalance;

    public AccountOpenedEvent(AccountNumber accountNumber, String accountHolderName, Money initialBalance) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.initialBalance = initialBalance;
    }

    @Override public UUID getEventId() { return eventId; }
    @Override public LocalDateTime getOccurredOn() { return occurredOn; }
    @Override public String getEventType() { return "ACCOUNT_OPENED"; }
    
    public AccountNumber getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public Money getInitialBalance() { return initialBalance; }
}