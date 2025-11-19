package bank.domain.entities;

public enum AccountType {
    SAVINGS("Savings Account", 0.02),
    CHECKING("Checking Account", 0.00);

    private final String description;
    private final double defaultInterestRate;

    AccountType(String description, double defaultInterestRate) {
        this.description = description;
        this.defaultInterestRate = defaultInterestRate;
    }

    public String getDescription() { return description; }
    public double getDefaultInterestRate() { return defaultInterestRate; }
}