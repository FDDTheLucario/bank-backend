package dev.soulcatcher.services;

import dev.soulcatcher.dtos.RegisterRequest;
import dev.soulcatcher.models.User;
import dev.soulcatcher.repos.UserRepository;
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

    public void createUser(RegisterRequest registerRequest) {
        User user = new User(registerRequest);
        user.setPassword(BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(10)));
        user.setAccounts(new ArrayList<>());
        userRepo.save(user);
    }
}
