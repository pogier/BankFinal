package bank.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

public final class AccountNumber {
    private static final Pattern PATTERN = Pattern.compile("^(SAV|CHK)\\d{6}$");
    private final String value;

    private AccountNumber(String value) {
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid account number format: " + value);
        }
        this.value = value;
    }

    public static AccountNumber of(String value) {
        return new AccountNumber(value);
    }

    public static AccountNumber savings(int sequence) {
        return new AccountNumber(String.format("SAV%06d", sequence));
    }

    public static AccountNumber checking(int sequence) {
        return new AccountNumber(String.format("CHK%06d", sequence));
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountNumber)) return false;
        AccountNumber that = (AccountNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public String toString() { return value; }
}