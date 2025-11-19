package bank.application.services;

import bank.application.commands.CreateAccountCommand;
import bank.application.commands.TransferFundsCommand;
import bank.domain.entities.*;
import bank.domain.events.FundsTransferredEvent;
import bank.domain.results.OperationResult;
import bank.domain.results.DepositSuccess;
import bank.domain.results.WithdrawalSuccess;
import bank.domain.valueobjects.AccountNumber;
import bank.domain.valueobjects.Money;
import bank.infrastructure.persistence.AccountRepository;
import bank.infrastructure.services.SequenceGenerator;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

public class DefaultAccountService implements AccountService {
    private final AccountRepository accountRepository;
    private final SequenceGenerator sequenceGenerator;
    private final Currency defaultCurrency;

    public DefaultAccountService(AccountRepository accountRepository, 
                               SequenceGenerator sequenceGenerator,
                               Currency defaultCurrency) {
        this.accountRepository = accountRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.defaultCurrency = defaultCurrency;
    }

    @Override
    public Account createAccount(CreateAccountCommand command) {
        AccountNumber accountNumber = generateAccountNumber(command.accountType());
        Account account = buildAccount(accountNumber, command);
        
        accountRepository.save(account);
        return account;
    }

    @Override
    public Optional<Account> findAccount(AccountNumber accountNumber) {
        return accountRepository.findByNumber(accountNumber);
    }

    @Override
    public OperationResult deposit(AccountNumber accountNumber, Money amount) {
        return accountRepository.findByNumber(accountNumber)
                .map(account -> {
                    OperationResult result = account.deposit(amount);
                    if (result instanceof DepositSuccess) {
                        accountRepository.save(account);
                    }
                    return result;
                })
                .orElse(new bank.domain.results.DepositFailed("Account not found"));
    }

    @Override
    public OperationResult withdraw(AccountNumber accountNumber, Money amount) {
        return accountRepository.findByNumber(accountNumber)
                .map(account -> {
                    OperationResult result = account.withdraw(amount);
                    if (result instanceof WithdrawalSuccess) {
                        accountRepository.save(account);
                    }
                    return result;
                })
                .orElse(new bank.domain.results.WithdrawalFailed("Account not found"));
    }

    @Override
    public void transferFunds(TransferFundsCommand command) {
        Account source = accountRepository.findByNumber(command.sourceAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        
        Account destination = accountRepository.findByNumber(command.destinationAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        // Atomic transfer operation
        OperationResult withdrawal = source.withdraw(command.amount());
        if (withdrawal instanceof WithdrawalSuccess) {
            OperationResult deposit = destination.deposit(command.amount());
            if (deposit instanceof DepositSuccess) {
                accountRepository.save(source);
                accountRepository.save(destination);
                
                // Record transfer event - now public method
                source.registerEvent(new FundsTransferredEvent(
                    command.sourceAccountNumber(),
                    command.destinationAccountNumber(),
                    command.amount(),
                    command.reference()
                ));
            } else {
                throw new IllegalStateException("Transfer failed during deposit");
            }
        } else {
            throw new IllegalStateException("Transfer failed during withdrawal");
        }
    }

    @Override
    public void applyInterest(AccountNumber accountNumber) {
        accountRepository.findByNumber(accountNumber)
                .filter(account -> account instanceof SavingsAccount)
                .map(account -> (SavingsAccount) account)
                .ifPresent(savingsAccount -> {
                    Money interest = savingsAccount.calculateInterest();
                    savingsAccount.deposit(interest);
                    accountRepository.save(savingsAccount);
                });
    }

    private AccountNumber generateAccountNumber(String accountType) {
        int sequence = sequenceGenerator.nextSequence(accountType);
        return accountType.equalsIgnoreCase("SAVINGS") 
                ? AccountNumber.savings(sequence)
                : AccountNumber.checking(sequence);
    }

    private Account buildAccount(AccountNumber accountNumber, CreateAccountCommand command) {
        if ("SAVINGS".equalsIgnoreCase(command.accountType())) {
            return new SavingsAccount(
                accountNumber,
                command.accountHolderName(),
                command.initialDeposit(),
                new BigDecimal("0.02"),
                Money.of(50.00, defaultCurrency)
            );
        } else if ("CHECKING".equalsIgnoreCase(command.accountType())) {
            return new CheckingAccount(
                accountNumber,
                command.accountHolderName(),
                command.initialDeposit(),
                Money.of(100.00, defaultCurrency)
            );
        } else {
            throw new IllegalArgumentException("Unknown account type: " + command.accountType());
        }
    }
}