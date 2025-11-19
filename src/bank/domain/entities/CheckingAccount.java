package bank.domain.entities;

import bank.domain.results.OperationResult;
import bank.domain.valueobjects.AccountNumber;
import bank.domain.valueobjects.Money;

public class CheckingAccount extends Account {
    private final Money overdraftLimit;

    public CheckingAccount(AccountNumber accountNumber, String accountHolderName, 
                          Money initialBalance, Money overdraftLimit) {
        super(accountNumber, accountHolderName, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public OperationResult withdraw(Money amount) {
        Money availableBalance = getBalance().add(overdraftLimit);
        return performWithdrawal(amount, availableBalance);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.CHECKING;
    }

    @Override
    public Money getAvailableBalance() {
        return getBalance().add(overdraftLimit);
    }

    public Money getOverdraftLimit() { return overdraftLimit; }
}