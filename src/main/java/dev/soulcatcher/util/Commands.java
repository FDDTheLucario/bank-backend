package dev.soulcatcher.util;

import de.vandermeer.asciitable.AsciiTable;
import dev.soulcatcher.dtos.NewAccountRequest;
import dev.soulcatcher.dtos.RegisterRequest;
import dev.soulcatcher.exceptions.ConflictException;
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

import java.util.List;
import java.util.Scanner;

@ShellComponent
public class Commands {
    private final UserRepository userRepo;
    private final String USER_NOT_FOUND = "Could not find any user with the username of %s. Check the spelling and try again.\n";
    private final String ACCOUNT_NOT_FOUND = "Could not find any account with the name of %s. Check the spelling and try again.\n";
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;

    public Commands(UserRepository userRepo, AccountRepository accountRepo, TransactionRepository transactionRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }
    @ShellMethod(value = "Creates a new account.")
    public void createAccount(String email, String firstName, String lastName, String username, String password) {
        AuthService authService = new AuthService(userRepo, accountRepo);
        RegisterRequest request = new RegisterRequest(email, username, firstName, lastName, password);
        authService.createUser(request);
        System.out.printf("Created user %s successfully!\n", username);
    }
    @ShellMethod(value = "Creates a new transaction.")
    public void createTransaction(String username, String accountName, double amount, String merchant) {
        UserService userService = new UserService(userRepo);
        TransactionService transService = new TransactionService(transactionRepo, accountRepo);
        AccountService accountService = new AccountService(accountRepo);
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (NotFoundException e) {
            System.out.printf(USER_NOT_FOUND, username);
            return;
        } catch (Throwable t) {
            System.out.println("An unknown error occurred.");
            return;
        }
        Account account;
        try {
            account = accountService.findByAccountNameAndUser(accountName, user);
        } catch (NotFoundException e) {
            System.out.printf(ACCOUNT_NOT_FOUND, accountName);
            return;
        } catch (Throwable t) {
            System.out.println("An unknown error occurred.");
            return;
        }
        Transaction transaction = new Transaction(account, amount, merchant);
        transService.addTransaction(transaction);
        System.out.printf("Created transaction with value of $%.2f. New account balance: $%.2f.\n", amount, account.getAvailableBalance());

    }
    @ShellMethod(value = "Adds a banking account to a user.")
    public void addBank(String username, @ShellOption(defaultValue = "Checking") String nickname, @ShellOption(defaultValue = "0.00") double startingBalance) {
        AccountService accountService = new AccountService(accountRepo);
        UserService userService = new UserService(userRepo);
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (NotFoundException e) {
            System.out.printf(USER_NOT_FOUND, username);
            return;
        }
        accountService.createAccount(new NewAccountRequest(nickname, user), startingBalance);
    }
    @ShellMethod(value = "Lists all available transactions for a user.")
    public void listUserTransactions(String username) {
        AsciiTable table = new AsciiTable();
        UserService userService = new UserService(userRepo);

        User user;
        try {
            user = userService.findByUsername(username);
        } catch (NotFoundException e) {
            System.out.printf(USER_NOT_FOUND, username);
            return;
        }
        System.out.printf("List of all transactions for %s.\n", username);
        List<Transaction> transactions = transactionRepo.
        System.out.println(table.render());
    }
}
