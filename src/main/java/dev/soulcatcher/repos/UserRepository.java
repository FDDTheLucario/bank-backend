package dev.soulcatcher.repos;

import dev.soulcatcher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query
    boolean existsByEmailIgnoreCase(String email);
    @Query
    boolean existsByUsernameIgnoreCase(String username);
    @Query
    User findByUsernameIgnoreCase(String username);
}
