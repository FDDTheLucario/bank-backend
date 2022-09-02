package dev.soulcatcher.repos;

import dev.soulcatcher.models.Transaction;
import dev.soulcatcher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    @Query
    List<Transaction> findAllByUser(User user);
}
