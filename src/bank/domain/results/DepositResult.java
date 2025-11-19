package bank.domain.results;

import bank.domain.valueobjects.Money;

public class DepositResult {
    public static OperationResult success(Money newBalance) { return new DepositSuccess(newBalance); }
    public static OperationResult failed(String reason) { return new DepositFailed(reason); }
}