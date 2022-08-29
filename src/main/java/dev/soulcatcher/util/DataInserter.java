package dev.soulcatcher.util;

import dev.soulcatcher.models.AccountType;
import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.repos.AccountTypeRepository;
import dev.soulcatcher.repos.TransactionRepository;
import dev.soulcatcher.repos.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final AccountTypeRepository acctTypeRepo;
    private final TransactionRepository transactionRepo;

    public DataInserter(UserRepository userRepo, AccountRepository accountRepo, AccountTypeRepository acctTypeRepo, TransactionRepository transactionRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.acctTypeRepo = acctTypeRepo;
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
        String[] accountTypes = {"Checking, Savings"};
        List<AccountType> types = new ArrayList<>();
        for (String s : accountTypes) {
            logger.info(String.format("Adding account type %s to account types", s));
            types.add(new AccountType(s.toUpperCase()));
            logger.info("Added successfully");
        }
        acctTypeRepo.saveAll(types);
    }
}
