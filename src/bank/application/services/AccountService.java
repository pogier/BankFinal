package bank.application.services;

import bank.application.commands.CreateAccountCommand;
import bank.application.commands.TransferFundsCommand;
import bank.domain.entities.Account;
import bank.domain.results.OperationResult;
import bank.domain.valueobjects.AccountNumber;
import bank.domain.valueobjects.Money;

import java.util.Optional;

public interface AccountService {
    Account createAccount(CreateAccountCommand command);
    Optional<Account> findAccount(AccountNumber accountNumber);
    OperationResult deposit(AccountNumber accountNumber, Money amount);
    OperationResult withdraw(AccountNumber accountNumber, Money amount);
    void transferFunds(TransferFundsCommand command);
    void applyInterest(AccountNumber accountNumber);
}