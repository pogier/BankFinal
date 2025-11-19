package bank.infrastructure.persistence;

import bank.domain.entities.Account;
import bank.domain.valueobjects.AccountNumber;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Optional<Account> findByNumber(AccountNumber accountNumber);
    List<Account> findAll();
    void delete(AccountNumber accountNumber);
    int count();
}