package dev.soulcatcher.services;

import dev.soulcatcher.dtos.AccountResponse;
import dev.soulcatcher.dtos.DetailedAccountResponse;
import dev.soulcatcher.dtos.NewAccountRequest;
import dev.soulcatcher.dtos.Principal;
import dev.soulcatcher.exceptions.NotFoundException;
import dev.soulcatcher.exceptions.UnauthorizedException;
import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.User;
import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.repos.UserRepository;
import dev.soulcatcher.services.token.TokenService;
import dev.soulcatcher.util.Generation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepo;
    private final TokenService tokenService;
    private final UserRepository userRepo;
    private final Logger logger = LogManager.getLogger();
    public AccountService(AccountRepository accountRepo, TokenService tokenService, UserRepository userRepo) {
        this.accountRepo = accountRepo;
        this.tokenService = tokenService;
        this.userRepo = userRepo;
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
    public List<AccountResponse> findAllByToken(String token) {
        Principal principal = tokenService.extractToken(token);
        List<AccountResponse> accountResponses = new ArrayList<>();
        User user = userRepo.findByUsernameIgnoreCase(principal.getUsername()).orElseThrow(UnauthorizedException::new);
        List<Account> userAccounts = accountRepo.findAllByUser(user);
        userAccounts.forEach(x -> accountResponses.add(new AccountResponse(x)));
        return accountResponses;
    }
    public DetailedAccountResponse getAccountInfo(String token, String accountId) {
        Principal principal = tokenService.extractToken(token);
        User user = userRepo.findByUsernameIgnoreCase(principal.getUsername()).orElseThrow(NotFoundException::new);
        Account account = accountRepo.findById(accountId).orElseThrow(NotFoundException::new);
        if (!account.getUser().getUserId().equals(principal.getAuthUserId())) {
            throw new UnauthorizedException();
        }
        return new DetailedAccountResponse(account);
    }
}
