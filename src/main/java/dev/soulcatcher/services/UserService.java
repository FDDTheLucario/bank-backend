package dev.soulcatcher.services;

import dev.soulcatcher.exceptions.NotFoundException;
import dev.soulcatcher.models.User;
import dev.soulcatcher.repos.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User findByUsername(String username) {
        return userRepo.findByUsernameIgnoreCase(username)
                       .map(User::new)
                       .orElseThrow(NotFoundException::new);
    }
}
