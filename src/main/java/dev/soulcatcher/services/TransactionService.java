package dev.soulcatcher.services;

import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.Transaction;
import dev.soulcatcher.repos.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;

    @Autowired
    public TransactionService(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }
    public void addTransaction(Account account, Transaction... transaction) {
        account.getTransactions().addAll(Arrays.asList(transaction));
        transactionRepo.saveAll(account.getTransactions());
    }
    public void addTransaction(double amount, String merchant, Account account) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAmount(amount);
        transaction.setMerchant(merchant);
        account.getTransactions().add(transaction);
        transactionRepo.save(transaction);
    }
}
