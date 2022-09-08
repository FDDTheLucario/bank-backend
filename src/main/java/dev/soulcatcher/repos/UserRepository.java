package dev.soulcatcher.repos;

import dev.soulcatcher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query
    boolean existsByEmailIgnoreCase(String email);
    @Query
    boolean existsByUsernameIgnoreCase(String username);
    @Query
    Optional<User> findByUsernameIgnoreCase(String username);
    @Query
    Optional<User> findByUsernameIgnoreCaseAndPassword(String username, String password);
}
