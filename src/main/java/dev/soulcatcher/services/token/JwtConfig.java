package dev.soulcatcher.services.token;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;


@Component
public class JwtConfig {
    @Value("${secrets.token-secret}")
    private String salt;
    @Value("#{24 * 60 * 60 * 1000}") // number of milliseconds in a day
    private int expiration;
    private final SignatureAlgorithm signatureAlgo = SignatureAlgorithm.HS256;
    private Key signingKey;

    @PostConstruct
    public void createSigningKey() {
        byte[] saltyBytes = DatatypeConverter.parseBase64Binary(salt);
        signingKey = new SecretKeySpec(saltyBytes, signatureAlgo.getJcaName());
    }

    public int getExpiration() {
        return expiration;
    }

    public SignatureAlgorithm getSignatureAlgo() {
        return signatureAlgo;
    }

    public Key getSigningKey() {
        return signingKey;
    }
}
