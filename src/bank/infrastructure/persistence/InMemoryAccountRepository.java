package bank.infrastructure.persistence;

import bank.domain.entities.Account;
import bank.domain.valueobjects.AccountNumber;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<AccountNumber, Account> accounts;

    public InMemoryAccountRepository() {
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    public void save(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public Optional<Account> findByNumber(AccountNumber accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void delete(AccountNumber accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public int count() {
        return accounts.size();
    }
}