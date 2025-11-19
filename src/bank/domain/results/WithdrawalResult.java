package bank.domain.results;

import bank.domain.valueobjects.Money;

public class WithdrawalResult {
    public static OperationResult success(Money newBalance) { return new WithdrawalSuccess(newBalance); }
    public static OperationResult failed(String reason) { return new WithdrawalFailed(reason); }
    public static OperationResult insufficientFunds(Money available, Money requested) { 
        return new InsufficientFunds(available, requested); 
    }
    public static OperationResult violatesMinimumBalance(Money minimum, Money resultingBalance) { 
        return new ViolatesMinimumBalance(minimum, resultingBalance); 
    }
}