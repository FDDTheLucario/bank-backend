package dev.soulcatcher.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.UUID;

public class Generation {
    public static long generateAccountNumber() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            long m = (long) Math.pow(10, 17 - 1);
            return Math.abs(m + (long) secureRandom.nextLong());
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
    public static String genId() {
        return UUID.randomUUID().toString();
    }
}
