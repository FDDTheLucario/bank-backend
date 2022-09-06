package dev.soulcatcher.repos;

import dev.soulcatcher.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, String> {

}
