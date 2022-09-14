package dev.soulcatcher.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.UUID;

public class Generation {
    public static long generateAccountNumber() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            int m = (int) Math.pow(10, 8);
            int firstPart = m + secureRandom.nextInt(9 * m);
            int secondPart = m + secureRandom.nextInt(9 * m);
            String numberAsString = Integer.toString(firstPart) + Integer.toString(secondPart);
            return Long.parseLong(numberAsString);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
    public static String genId() {
        return UUID.randomUUID().toString();
    }
    public static String getLastFour(long input) {
        return String.valueOf(input).substring(String.valueOf(input).length() - 4);
    }
}
