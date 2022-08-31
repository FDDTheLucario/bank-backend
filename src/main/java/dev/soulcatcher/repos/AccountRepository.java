package dev.soulcatcher.repos;

import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query
    Account findByNicknameIgnoreCaseAndUser(String nickname, User user);
}
