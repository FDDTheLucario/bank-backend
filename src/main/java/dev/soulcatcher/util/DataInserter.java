package dev.soulcatcher.util;

import dev.soulcatcher.dtos.RegisterRequest;
import dev.soulcatcher.exceptions.ConflictException;
import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.User;
import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.repos.TransactionRepository;
import dev.soulcatcher.repos.UserRepository;
import dev.soulcatcher.services.AccountService;
import dev.soulcatcher.services.AuthService;
import dev.soulcatcher.services.TransactionService;
import lombok.var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
@Profile("default || local || test")
public class DataInserter implements CommandLineRunner {
    @Value("${run-inserter}")
    private boolean canRunInserter;
    private final Logger logger = LogManager.getLogger();
    private final UserRepository userRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;

    public DataInserter(UserRepository userRepo, AccountRepository accountRepo, TransactionRepository transactionRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }
    @Override
    public void run(String... args) throws Exception {
        final AuthService authService = new AuthService(userRepo, accountRepo);
        logger.info("Checking if the data inserter should be run...");
        if (!canRunInserter) {
            logger.info("Data inserter cannot run; continuing as usual.");
            return;
        }
        logger.info("Running inserter...");
        logger.info("Currently creating new user...");
        try {
            RegisterRequest registerRequest = new RegisterRequest();
            logger.info("Creating test register request...");
            registerRequest.setEmail("testing@testing.com");
            logger.info(String.format("Set email as %s", registerRequest.getEmail()));
            registerRequest.setFirstName("Tester");
            logger.info(String.format("Set first name as %s", registerRequest.getFirstName()));
            registerRequest.setLastName("McTesterson");
            logger.info(String.format("Set last name as %s", registerRequest.getLastName()));
            registerRequest.setPassword("testing123");
            logger.info(String.format("Set password as %s", registerRequest.getPassword()));
            registerRequest.setUsername("testermctest0");
            logger.info(String.format("Set username as %s", registerRequest.getUsername()));
            authService.createUser(registerRequest);
            logger.info("Created user successfully");
        } catch (ConflictException e) {
            logger.info("User already exists; skipping...");
        } catch (Throwable t) {
            logger.info("Catastrophic failure");
            throw new RuntimeException(t);
        }
        System.out.print("Create test transaction? (Y/n)");
        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine().toLowerCase();
        sc.close();

        if (in.equals("") | in.equals("y")) {
            createTestTransaction();
        } else {
            logger.info("Continuing without creating transaction");
        }
    }
    public void createTestTransaction() {
        final TransactionService transService = new TransactionService(transactionRepo, accountRepo);
        logger.info("Creating test transaction");
        try {
            Account account = accountRepo.findByNicknameIgnoreCaseAndUser("Checking", userRepo.findByUsernameIgnoreCase("testermctest0"));

            transService.addTransaction(420.00, "Test Merchant", account);
            logger.info("Created successfully");
        } catch (Throwable t) {
            logger.info("Could not create transaction");
            t.printStackTrace();
        }
    }
}
