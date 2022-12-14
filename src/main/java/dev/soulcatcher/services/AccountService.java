package dev.soulcatcher.services;

import dev.soulcatcher.dtos.NewAccountRequest;
import dev.soulcatcher.exceptions.NotFoundException;
import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.User;
import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.util.Generation;
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
    public void createAccount(NewAccountRequest request) {
        Account account = new Account(request);
        account.setAccountId(Generation.genId());
        account.setAccountNumber(Generation.generateAccountNumber());
        account.setTransactions(new ArrayList<>());
        account.setCurrentBalance(0);
        account.setUser(request.getUser());
        accountRepo.save(account);
        logger.info(String.format("Created new bank account %d for %s.", account.getAccountNumber(), account.getUser().getUsername()));
    }
    public void createAccount(NewAccountRequest request, double startingAmount) {
        Account account = new Account(request);
        account.setAccountId(Generation.genId());
        account.setAccountNumber(Generation.generateAccountNumber());
        account.setTransactions(new ArrayList<>());
        account.setCurrentBalance(startingAmount);
        account.setUser(request.getUser());
        accountRepo.save(account);
        logger.info(String.format("Created new bank account %d for %s.", account.getAccountNumber(), account.getUser().getUsername()));
    }
    public Account findByAccountNameAndUser(String nickname, User user) {
        return accountRepo.findByNicknameIgnoreCaseAndUser(nickname, user)
                          .map(Account::new)
                          .orElseThrow(NotFoundException::new);
    }
}
