package bank.presentation.console;

import bank.application.services.AccountService;
import bank.domain.entities.Account;
import bank.domain.results.OperationResult;
import bank.domain.results.DepositSuccess;
import bank.domain.results.WithdrawalSuccess;
import bank.domain.results.DepositFailed;
import bank.domain.results.WithdrawalFailed;
import bank.domain.results.InsufficientFunds;
import bank.domain.results.ViolatesMinimumBalance;
import bank.domain.valueobjects.AccountNumber;
import bank.domain.valueobjects.Money;

import java.util.Currency;
import java.util.Scanner;

public class BankConsoleApp {
    private final AccountService accountService;
    private final ConsoleInputHandler inputHandler;
    private final Currency defaultCurrency;
    private boolean running;

    public BankConsoleApp(AccountService accountService, Currency defaultCurrency) {
        this.accountService = accountService;
        this.defaultCurrency = defaultCurrency;
        this.inputHandler = new ConsoleInputHandler(new Scanner(System.in));
        this.running = true;
    }

    public void start() {
        System.out.println("=== 🏦 Enterprise Java Bank ===");
        
        while (running) {
            new MainMenu().display();
        }
        
        System.out.println("Thank you for banking with us! 👋");
    }

    private class MainMenu extends ConsoleMenu {
        public MainMenu() {
            super(BankConsoleApp.this.inputHandler);
        }

        @Override
        protected void initializeOptions() {
            options.put(1, new MenuOption("Create New Account", this::createAccount));
            options.put(2, new MenuOption("Access Existing Account", this::accessAccount));
            options.put(3, new MenuOption("Admin Functions", this::adminFunctions));
            options.put(4, new MenuOption("Exit", () -> running = false));
        }

        @Override
        protected void displayHeader() {
            System.out.println("\n=== MAIN MENU ===");
        }

        private void createAccount() {
            System.out.println("\n--- Create New Account ---");
            
            String name = inputHandler.readString("Enter account holder name: ");
            double amount = inputHandler.readDouble("Enter initial deposit: $", a -> a > 0);
            
            System.out.println("Select account type:");
            System.out.println("1. Savings Account");
            System.out.println("2. Checking Account");
            
            int typeChoice = inputHandler.readInt("Enter choice (1-2): ", c -> c == 1 || c == 2);
            
            String accountType = typeChoice == 1 ? "SAVINGS" : "CHECKING";
            
            try {
                Account account = accountService.createAccount(
                    bank.application.commands.CreateAccountCommand.of(name, amount, defaultCurrency.getCurrencyCode(), accountType)
                );
                
                System.out.println("\n✅ Account created successfully!");
                System.out.println("Account Number: " + account.getAccountNumber());
                System.out.println("Account Holder: " + account.getAccountHolderName());
                System.out.println("Account Type: " + account.getAccountType());
                System.out.println("Initial Balance: " + account.getBalance());
            } catch (Exception e) {
                System.out.println("❌ Error creating account: " + e.getMessage());
            }
        }

        private void accessAccount() {
            System.out.println("\n--- Access Account ---");
            String accountNumber = inputHandler.readString("Enter account number: ");
            
            try {
                AccountNumber accNumber = AccountNumber.of(accountNumber);
                accountService.findAccount(accNumber)
                        .ifPresentOrElse(
                            this::showAccountMenu,
                            () -> System.out.println("❌ Account not found: " + accountNumber)
                        );
            } catch (Exception e) {
                System.out.println("❌ Invalid account number: " + e.getMessage());
            }
        }

        private void showAccountMenu(Account account) {
            new AccountMenu(account).display();
        }

        private void adminFunctions() {
            new AdminMenu().display();
        }
    }

    private class AccountMenu extends ConsoleMenu {
        private final Account account;

        public AccountMenu(Account account) {
            super(BankConsoleApp.this.inputHandler);
            this.account = account;
        }

        @Override
        protected void initializeOptions() {
            options.put(1, new MenuOption("Deposit Funds", this::deposit));
            options.put(2, new MenuOption("Withdraw Funds", this::withdraw));
            options.put(3, new MenuOption("View Account Details", this::viewDetails));
            options.put(4, new MenuOption("View Available Balance", this::viewAvailableBalance));
        }

        @Override
        protected void displayHeader() {
            System.out.printf("\n--- Account: %s ---%n", account.getAccountNumber());
            System.out.printf("Holder: %s | Balance: %s%n", 
                account.getAccountHolderName(), account.getBalance());
        }

        private void deposit() {
            double amount = inputHandler.readDouble("Enter deposit amount: $", a -> a > 0);
            Money depositAmount = Money.of(amount, defaultCurrency);
            
            OperationResult result = accountService.deposit(account.getAccountNumber(), depositAmount);
            handleOperationResult(result, "deposit");
        }

        private void withdraw() {
            double amount = inputHandler.readDouble("Enter withdrawal amount: $", a -> a > 0);
            Money withdrawalAmount = Money.of(amount, defaultCurrency);
            
            OperationResult result = accountService.withdraw(account.getAccountNumber(), withdrawalAmount);
            handleOperationResult(result, "withdrawal");
        }

        private void viewDetails() {
            System.out.println("\n--- Account Details ---");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Holder: " + account.getAccountHolderName());
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Current Balance: " + account.getBalance());
            System.out.println("Available Balance: " + account.getAvailableBalance());
            System.out.println("Date Created: " + account.getDateCreated());
        }

        private void viewAvailableBalance() {
            System.out.printf("Available Balance: %s%n", account.getAvailableBalance());
        }

        private void handleOperationResult(OperationResult result, String operation) {
            if (result instanceof DepositSuccess success) {
                System.out.printf("✅ %s successful! New Balance: %s%n", 
                    operation.substring(0, 1).toUpperCase() + operation.substring(1),
                    success.getNewBalance());
            } else if (result instanceof WithdrawalSuccess success) {
                System.out.printf("✅ %s successful! New Balance: %s%n", 
                    operation.substring(0, 1).toUpperCase() + operation.substring(1),
                    success.getNewBalance());
            } else if (result instanceof DepositFailed failed) {
                System.out.printf("❌ %s failed: %s%n", operation, failed.getReason());
            } else if (result instanceof WithdrawalFailed failed) {
                System.out.printf("❌ %s failed: %s%n", operation, failed.getReason());
            } else if (result instanceof InsufficientFunds insufficient) {
                System.out.printf("❌ Insufficient funds. Available: %s, Requested: %s%n", 
                    insufficient.getAvailable(), insufficient.getRequested());
            } else if (result instanceof ViolatesMinimumBalance violates) {
                System.out.printf("❌ Would violate minimum balance. Minimum: %s, Resulting: %s%n", 
                    violates.getMinimum(), violates.getResultingBalance());
            } else {
                System.out.println("❌ Unexpected result type");
            }
        }
    }

    private class AdminMenu extends ConsoleMenu {
        public AdminMenu() {
            super(BankConsoleApp.this.inputHandler);
        }

        @Override
        protected void initializeOptions() {
            options.put(1, new MenuOption("View All Accounts", this::viewAllAccounts));
            options.put(2, new MenuOption("Apply Interest to Savings", this::applyInterest));
            options.put(3, new MenuOption("Bank Statistics", this::showStatistics));
        }

        @Override
        protected void displayHeader() {
            System.out.println("\n--- Admin Functions ---");
        }

        private void viewAllAccounts() {
            System.out.println("Feature: View All Accounts - To be implemented");
        }

        private void applyInterest() {
            System.out.println("Feature: Apply Interest - To be implemented");
        }

        private void showStatistics() {
            System.out.println("Feature: Bank Statistics - To be implemented");
        }
    }

    public static void main(String[] args) {
        // Dependency injection setup
        var repository = new bank.infrastructure.persistence.InMemoryAccountRepository();
        var sequenceGenerator = new bank.infrastructure.services.SequenceGenerator();
        var accountService = new bank.application.services.DefaultAccountService(
            repository, sequenceGenerator, Currency.getInstance("USD")
        );
        
        BankConsoleApp app = new BankConsoleApp(accountService, Currency.getInstance("USD"));
        app.start();
    }
}