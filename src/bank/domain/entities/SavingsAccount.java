package bank.domain.entities;

import bank.domain.results.OperationResult;
import bank.domain.valueobjects.AccountNumber;
import bank.domain.valueobjects.Money;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
    private final BigDecimal interestRate;
    private final Money minimumBalance;

    public SavingsAccount(AccountNumber accountNumber, String accountHolderName, 
                         Money initialBalance, BigDecimal interestRate, Money minimumBalance) {
        super(accountNumber, accountHolderName, initialBalance);
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    @Override
    public OperationResult withdraw(Money amount) {
        Money potentialBalance = getBalance().subtract(amount);
        
        if (potentialBalance.isLessThan(minimumBalance)) {
            return bank.domain.results.WithdrawalResult.violatesMinimumBalance(minimumBalance, potentialBalance);
        }

        return performWithdrawal(amount, getBalance());
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }

    @Override
    public Money getAvailableBalance() {
        return getBalance().subtract(minimumBalance);
    }

    public Money calculateInterest() {
        return Money.of(
            getBalance().getAmount().multiply(interestRate), 
            getBalance().getCurrency()
        );
    }

    public BigDecimal getInterestRate() { return interestRate; }
    public Money getMinimumBalance() { return minimumBalance; }
}