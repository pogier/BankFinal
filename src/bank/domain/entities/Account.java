package bank.domain.entities;

import bank.domain.events.*;
import bank.domain.results.OperationResult;
import bank.domain.valueobjects.AccountNumber;
import bank.domain.valueobjects.Money;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Account {
    private final AccountNumber accountNumber;
    private String accountHolderName;
    private Money balance;
    private final LocalDateTime dateCreated;
    private final List<DomainEvent> domainEvents;

    protected Account(AccountNumber accountNumber, String accountHolderName, Money initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.dateCreated = LocalDateTime.now();
        this.domainEvents = new ArrayList<>();
        
        registerEvent(new AccountOpenedEvent(accountNumber, accountHolderName, initialBalance));
    }

    // Core business logic - use full class names
    public final OperationResult deposit(Money amount) {
        if (!amount.isPositive()) {
            return bank.domain.results.DepositResult.failed("Deposit amount must be positive");
        }

        Money oldBalance = this.balance;
        this.balance = this.balance.add(amount);
        
        registerEvent(new FundsDepositedEvent(accountNumber, amount, oldBalance, this.balance));
        return bank.domain.results.DepositResult.success(this.balance);
    }

    public abstract OperationResult withdraw(Money amount);

    protected final OperationResult performWithdrawal(Money amount, Money availableBalance) {
        if (!amount.isPositive()) {
            return bank.domain.results.WithdrawalResult.failed("Withdrawal amount must be positive");
        }

        if (amount.isGreaterThan(availableBalance)) {
            return bank.domain.results.WithdrawalResult.insufficientFunds(availableBalance, amount);
        }

        Money oldBalance = this.balance;
        this.balance = this.balance.subtract(amount);
        
        registerEvent(new FundsWithdrawnEvent(accountNumber, amount, oldBalance, this.balance));
        return bank.domain.results.WithdrawalResult.success(this.balance);
    }

    // Event sourcing - make this public
    public void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    // Getters
    public AccountNumber getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public Money getBalance() { return balance; }
    public LocalDateTime getDateCreated() { return dateCreated; }

    // Behavioral methods
    public abstract AccountType getAccountType();
    public abstract Money getAvailableBalance();
}