package dev.soulcatcher.services;

import dev.soulcatcher.dtos.NewAccountRequest;
import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.User;
import dev.soulcatcher.repos.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;

@Service
public class AccountService {

    private final AccountRepository accountRepo;
    private final Logger logger = LogManager.getLogger();
    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }
    private long generateAccountNumber() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            long m = (long) Math.pow(10, 17 - 1);
            return Math.abs(m + (long) secureRandom.nextLong());
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
    public void createAccount(NewAccountRequest request) {
        Account account = new Account(request);
        account.setAccountNumber(generateAccountNumber());
        account.setTransactions(new ArrayList<>());
        account.setCurrentBalance(0);
        account.setAvailableBalance(0);
        account.setUser(request.getUser());
        accountRepo.save(account);
        logger.info(String.format("Created new bank account %d for %s.", account.getAccountNumber(), account.getUser().getUsername()));
    }

}
