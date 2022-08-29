package dev.soulcatcher.services;

import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

@Service
public class AccountService {
    private long generateAccountNumber() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            long m = (long) Math.pow(10, 17 - 1);
            return Math.abs(m + (long) secureRandom.nextLong());
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
}
