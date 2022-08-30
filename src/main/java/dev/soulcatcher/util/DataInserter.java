package dev.soulcatcher.util;

import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.repos.TransactionRepository;
import dev.soulcatcher.repos.UserRepository;
import dev.soulcatcher.services.AccountService;
import dev.soulcatcher.services.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        logger.info("Checking if the data inserter should be run...");
        if (!canRunInserter) {
            logger.info("Data inserter cannot run; continuing as usual.");
            return;
        }
        logger.info("Running inserter...");

    }
}
