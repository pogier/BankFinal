package bank.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DomainEvent {
    UUID getEventId();
    LocalDateTime getOccurredOn();
    String getEventType();
}