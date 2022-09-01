package dev.soulcatcher.util;

import dev.soulcatcher.dtos.RegisterRequest;
import dev.soulcatcher.exceptions.ConflictException;
import dev.soulcatcher.models.Account;
import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.repos.TransactionRepository;
import dev.soulcatcher.repos.UserRepository;
import dev.soulcatcher.services.AccountService;
import dev.soulcatcher.services.AuthService;
import dev.soulcatcher.services.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Scanner;

@ShellComponent
public class Commands {
    private final UserRepository userRepo;
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

    }
}
