package bank.domain.results;

public class DepositFailed implements OperationResult {
    private final String reason;
    public DepositFailed(String reason) { this.reason = reason; }
    public String getReason() { return reason; }
}