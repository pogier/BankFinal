package bank.domain.events;

import bank.domain.valueobjects.AccountNumber;
import bank.domain.valueobjects.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public class FundsTransferredEvent implements DomainEvent {
    private final UUID eventId;
    private final LocalDateTime occurredOn;
    private final AccountNumber sourceAccount;
    private final AccountNumber destinationAccount;
    private final Money amount;
    private final String reference;

    public FundsTransferredEvent(AccountNumber source, AccountNumber destination, Money amount, String reference) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
        this.sourceAccount = source;
        this.destinationAccount = destination;
        this.amount = amount;
        this.reference = reference;
    }

    @Override public UUID getEventId() { return eventId; }
    @Override public LocalDateTime getOccurredOn() { return occurredOn; }
    @Override public String getEventType() { return "FUNDS_TRANSFERRED"; }
    
    public AccountNumber getSourceAccount() { return sourceAccount; }
    public AccountNumber getDestinationAccount() { return destinationAccount; }
    public Money getAmount() { return amount; }
    public String getReference() { return reference; }
}