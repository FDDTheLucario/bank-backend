package dev.soulcatcher.services;

import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.Transaction;
import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.repos.TransactionRepository;
import dev.soulcatcher.util.Generation;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;
    @Autowired
    public TransactionService(TransactionRepository transactionRepo, AccountRepository accountRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
    }
    public void addTransaction(Transaction... transaction) {
        for (Transaction t : transaction) {
            Account account = t.getAccount();
            double balance = account.getCurrentBalance();
            account.getTransactions().add(t);
            transactionRepo.save(t);
            account.setAvailableBalance(balance - t.getAmount());
            accountRepo.save(account);
        }
    }
    public void addTransaction(double amount, String merchant, Account account) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        double balance = transaction.getAccount().getCurrentBalance();
        transaction.setTransactionId(Generation.genId());
        transaction.setAmount(amount);
        transaction.setMerchant(merchant);
        Hibernate.initialize(account.getTransactions());
        account.getTransactions().add(transaction);

        transaction.getAccount().setAvailableBalance(amount - balance);
        transactionRepo.save(transaction);
        accountRepo.save(account);
    }
}
