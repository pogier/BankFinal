package bank.domain.results;

public class WithdrawalFailed implements OperationResult {
    private final String reason;
    public WithdrawalFailed(String reason) { this.reason = reason; }
    public String getReason() { return reason; }
}