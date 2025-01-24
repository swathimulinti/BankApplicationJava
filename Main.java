package bankapplicationjava;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Abstract Account Class (Base Class)
abstract class Account {
    private String accountNumber;
    private String accountHolderName;
    private double balance;

    public Account(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }

    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account Holder: " + accountHolderName + ", Account Number: " + accountNumber + ", Balance: $" + balance;
    }
}

// Savings Account Class (Inherits from Account)
class SavingsAccount extends Account {
    private static final double MIN_BALANCE = 500;

    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            double newBalance = getBalance() + amount;
            setBalance(newBalance);
            System.out.println("Deposited $" + amount + ". New balance: $" + getBalance());
        } else {
            System.out.println("Deposit amount must be greater than zero.");
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && (getBalance() - amount) >= MIN_BALANCE) {
            double newBalance = getBalance() - amount;
            setBalance(newBalance);
            System.out.println("Withdrawn $" + amount + ". New balance: $" + getBalance());
        } else {
            System.out.println("Withdrawal exceeds balance or falls below minimum balance of $" + MIN_BALANCE);
        }
    }
}

// Checking Account Class (Inherits from Account)
class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 1000;

    public CheckingAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            double newBalance = getBalance() + amount;
            setBalance(newBalance);
            System.out.println("Deposited $" + amount + ". New balance: $" + getBalance());
        } else {
            System.out.println("Deposit amount must be greater than zero.");
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && getBalance() - amount >= -OVERDRAFT_LIMIT) {
            double newBalance = getBalance() - amount;
            setBalance(newBalance);
            System.out.println("Withdrawn $" + amount + ". New balance: $" + getBalance());
        } else {
            System.out.println("Insufficient funds or exceeds overdraft limit.");
        }
    }
}

// Bank Class to Manage Accounts
class Bank {
    private Map<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }

    public void createAccount(String accountType, String accountNumber, String accountHolderName, double initialBalance) {
        Account account = null;
        if (accountType.equalsIgnoreCase("savings")) {
            account = new SavingsAccount(accountNumber, accountHolderName, initialBalance);
        } else if (accountType.equalsIgnoreCase("checking")) {
            account = new CheckingAccount(accountNumber, accountHolderName, initialBalance);
        }

        if (account != null) {
            accounts.put(accountNumber, account);
            System.out.println("Account created successfully: " + account);
        } else {
            System.out.println("Invalid account type.");
        }
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void transferMoney(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = getAccount(fromAccountNumber);
        Account toAccount = getAccount(toAccountNumber);

        if (fromAccount != null && toAccount != null) {
            if (fromAccount.getBalance() >= amount) {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
                System.out.println("Transferred $" + amount + " from " + fromAccountNumber + " to " + toAccountNumber);
            } else {
                System.out.println("Insufficient funds in source account.");
            }
        } else {
            System.out.println("Invalid account numbers.");
        }
    }

    public void showBalance(String accountNumber) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            System.out.println("Account balance for " + accountNumber + ": $" + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }
}

// Main Class for Running the Banking System
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();

        // User input for account creation
        System.out.println("Enter account type (savings/checking): ");
        String accountType = scanner.nextLine();

        System.out.println("Enter account number: ");
        String accountNumber = scanner.nextLine();

        System.out.println("Enter account holder name: ");
        String accountHolderName = scanner.nextLine();

        System.out.println("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline left by nextDouble()

        bank.createAccount(accountType, accountNumber, accountHolderName, initialBalance);

        // User interaction loop for deposit, withdraw, and transfer
        while (true) {
            System.out.println("\nChoose an action: ");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer Money");
            System.out.println("4. Show Balance");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 1) {
                // Deposit
                System.out.println("Enter account number: ");
                String accountNum = scanner.nextLine();
                Account account = bank.getAccount(accountNum);
                if (account != null) {
                    System.out.println("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                } else {
                    System.out.println("Account not found.");
                }
            } else if (choice == 2) {
                // Withdraw
                System.out.println("Enter account number: ");
                String accountNum = scanner.nextLine();
                Account account = bank.getAccount(accountNum);
                if (account != null) {
                    System.out.println("Enter withdrawal amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    account.withdraw(withdrawAmount);
                } else {
                    System.out.println("Account not found.");
                }
            } else if (choice == 3) {
                // Transfer money
                System.out.println("Enter from account number: ");
                String fromAccount = scanner.nextLine();
                System.out.println("Enter to account number: ");
                String toAccount = scanner.nextLine();
                System.out.println("Enter transfer amount: ");
                double transferAmount = scanner.nextDouble();
                bank.transferMoney(fromAccount, toAccount, transferAmount);
            } else if (choice == 4) {
                // Show balance
                System.out.println("Enter account number: ");
                String accountNum = scanner.nextLine();
                bank.showBalance(accountNum);
            } else if (choice == 5) {
                System.out.println("Exiting the banking system.");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
