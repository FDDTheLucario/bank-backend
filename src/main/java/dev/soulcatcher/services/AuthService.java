package dev.soulcatcher.services;

import dev.soulcatcher.dtos.AuthResponse;
import dev.soulcatcher.dtos.RegisterRequest;
import dev.soulcatcher.exceptions.NotFoundException;
import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.User;
import dev.soulcatcher.repos.UserRepository;
import dev.soulcatcher.util.Generation;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final Logger logger = LogManager.getLogger();
    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @SneakyThrows
    public AuthResponse createUser(RegisterRequest registerRequest) {
        User user = new User(registerRequest);
        registerRequest.setPassword(BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(10)));
        user.setAccounts(new ArrayList<>());
        Account account = new Account();
        account.setUser(user);
        account.setNickname("Checking");
        account.setAccountNumber(Generation.generateAccountNumber());
        user.getAccounts().add(account);
        userRepo.save(user);
        return userRepo.findById(user.getUserId()).map(AuthResponse::new).orElseThrow(NotFoundException::new);
    }
}
