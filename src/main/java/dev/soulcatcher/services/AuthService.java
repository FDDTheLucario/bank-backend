package dev.soulcatcher.services;

import dev.soulcatcher.dtos.AuthResponse;
import dev.soulcatcher.dtos.LoginRequest;
import dev.soulcatcher.dtos.NewAccountRequest;
import dev.soulcatcher.dtos.RegisterRequest;
import dev.soulcatcher.exceptions.ConflictException;
import dev.soulcatcher.exceptions.NotFoundException;
import dev.soulcatcher.models.User;
import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.repos.UserRepository;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final AccountRepository accountRepo;
    private final Logger logger = LogManager.getLogger();
    final AccountService accountService;
    @Autowired
    public AuthService(UserRepository userRepo, AccountRepository accountRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.accountService = new AccountService(accountRepo);
    }

    @SneakyThrows
    public void createUser(RegisterRequest registerRequest) {
        User user = new User(registerRequest);
        if (userRepo.existsByEmailIgnoreCase(registerRequest.getEmail()) || userRepo.existsByUsernameIgnoreCase(registerRequest.getUsername())) {
            throw new ConflictException("User info already exists");
        }
        user.setPassword(BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(10)));
        user.setAccounts(new ArrayList<>());

        userRepo.save(user);
        accountService.createAccount(new NewAccountRequest("Checking", user));
    }
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(LoginRequest loginRequest) {
        User u = userRepo.findByUsernameIgnoreCase(loginRequest.getUsername()).get();
        return userRepo.findByUsernameIgnoreCaseAndPassword(loginRequest.getUsername(), u.getPassword())
                .map(AuthResponse::new)
                .orElseThrow(NotFoundException::new);
    }
}
