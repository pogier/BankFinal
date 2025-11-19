package bank.infrastructure.services;

import java.util.concurrent.atomic.AtomicInteger;

public class SequenceGenerator {
    private final AtomicInteger savingsSequence = new AtomicInteger(1);
    private final AtomicInteger checkingSequence = new AtomicInteger(1);

    public int nextSequence(String accountType) {
        return switch (accountType.toUpperCase()) {
            case "SAVINGS" -> savingsSequence.getAndIncrement();
            case "CHECKING" -> checkingSequence.getAndIncrement();
            default -> throw new IllegalArgumentException("Unknown account type: " + accountType);
        };
    }
}