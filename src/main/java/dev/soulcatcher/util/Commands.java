package dev.soulcatcher.util;

import de.vandermeer.asciitable.AsciiTable;
import dev.soulcatcher.dtos.NewAccountRequest;
import dev.soulcatcher.dtos.RegisterRequest;
import dev.soulcatcher.exceptions.ConflictException;
import dev.soulcatcher.exceptions.InsufficientFundsException;
import dev.soulcatcher.exceptions.NotFoundException;
import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.Transaction;
import dev.soulcatcher.models.User;
import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.repos.TransactionRepository;
import dev.soulcatcher.repos.UserRepository;
import dev.soulcatcher.services.AccountService;
import dev.soulcatcher.services.AuthService;
import dev.soulcatcher.services.TransactionService;
import dev.soulcatcher.services.UserService;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;


@ShellComponent
public class Commands {
    private final String USER_NOT_FOUND = "Could not find any user with the username of %s. Check the spelling and try again.\n";
    private final String ACCOUNT_NOT_FOUND = "Could not find any account with the name of %s. Check the spelling and try again.\n";

    private final String INSUFFICIENT_FUNDS = "Account %s has insufficient funds to transfer. This transfer requires $%.2f more than the balance available.\n";
    private final UserRepository userRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;
    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transService;
    private final AuthService authService;

    public Commands(UserRepository userRepo, AccountRepository accountRepo, TransactionRepository transactionRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
        userService = new UserService(userRepo);
        accountService = new AccountService(accountRepo);
        transService = new TransactionService(transactionRepo, accountRepo);
        authService = new AuthService(userRepo, accountRepo);
    }
    @ShellMethod(value = "Creates a new account.")
    public void createAccount(String email, String firstName, String lastName, String username, String password) {
        RegisterRequest request = new RegisterRequest(email, username, firstName, lastName, password);
        authService.createUser(request);
        System.out.printf("Created user %s successfully!\n", username);
    }
    @ShellMethod(value = "Creates a new transaction.")
    public void createTransaction(String username, String accountName, double amount, String merchant) {
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (NotFoundException e) {
            System.err.printf(USER_NOT_FOUND, username);
            return;
        } catch (Throwable t) {
            System.err.println("An unknown error occurred.");
            return;
        }
        Account account;
        try {
            account = accountService.findByAccountNameAndUser(accountName, user);
        } catch (NotFoundException e) {
            System.out.printf(ACCOUNT_NOT_FOUND, accountName);
            return;
        } catch (Throwable t) {
            System.err.println("An unknown error occurred.");
            return;
        }
        Transaction transaction = new Transaction(account, amount, merchant);
        transService.addTransaction(transaction);
        System.out.printf("Created transaction with value of $%.2f. New account balance: $%.2f.\n", amount, account.getAvailableBalance());

    }
    @ShellMethod(value = "Adds a banking account to a user.")
    public void addBank(String username, @ShellOption(defaultValue = "Checking") String nickname, @ShellOption(defaultValue = "0.00") double startingBalance) {
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (NotFoundException e) {
            System.err.printf(USER_NOT_FOUND, username);
            return;
        }
        accountService.createAccount(new NewAccountRequest(nickname, user), startingBalance);
    }
    @ShellMethod(value = "Lists every user's transacation.")
    public void listUserTransactions(String username) {
        AsciiTable table = new AsciiTable();
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (NotFoundException e) {
            System.err.printf(USER_NOT_FOUND, username);
            return;
        }
        System.out.printf("List of all transactions for %s.\n", username);
        table.addRow("Account", "Transaction ID", "Amount", "Merchant");
        List<Account> accounts = accountRepo.findAllByUser(user);
        for (Account a : accounts) {
            List<Transaction> transactions = transactionRepo.findAllByAccount(a);
            for (Transaction t : transactions) {
                table.addRule();
                table.addRow(a.getNickname(), t.getTransactionId(), String.format("$%.2f", t.getAmount()), t.getMerchant());
            }
        }
        System.out.println(table.render());
    }
    @ShellMethod(value = "Transfers money between a user's own accounts.")
    public void transferMoney(String username, double amount, String fromAccount, String toAccount) {
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (NotFoundException e) {
            System.out.printf(USER_NOT_FOUND, username);
            return;
        }

        Account from;
        Account recipient;
        try {
            from = accountService.findByAccountNameAndUser(fromAccount, user);
        } catch (NotFoundException e) {
            System.err.printf(ACCOUNT_NOT_FOUND, fromAccount);
            return;
        }
        try {
            recipient = accountService.findByAccountNameAndUser(toAccount, user);
            transService.transferMoney(amount, from, recipient);
            System.out.printf("Transferred $%.2f from %s to %s successfully.\n", amount, fromAccount, toAccount);
        } catch (InsufficientFundsException e) {
            System.err.printf(INSUFFICIENT_FUNDS, fromAccount, Math.abs(amount - from.getAvailableBalance()));
            return;
        } catch (NotFoundException e) {
            System.out.printf(ACCOUNT_NOT_FOUND, toAccount);
            return;
        }
    }
}
