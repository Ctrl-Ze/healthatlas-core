package com.healthatlas.core.bloodtests.util;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TestKeyResource implements QuarkusTestResourceLifecycleManager {

    public static String PRIVATE_KEY_URI;
    private Path privateKeyPath;
    private Path publicKeyPath;

    @Override
    public Map<String, String> start() {
        try {
            // Generate RSA key pair
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair keyPair = kpg.generateKeyPair();

            // Write PKCS#8 private key in PEM format
            privateKeyPath = Files.createTempFile("jwt-private", ".pem");
            String privatePem = "-----BEGIN PRIVATE KEY-----\n" +
                    Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.UTF_8)) // <-- KEY CHANGE: Using MimeEncoder
                            .encodeToString(keyPair.getPrivate().getEncoded()) +
                    "\n-----END PRIVATE KEY-----\n";
            Files.writeString(privateKeyPath, privatePem, StandardCharsets.UTF_8);

            PRIVATE_KEY_URI = privateKeyPath.toUri().toString();

            // Write public key in PEM format
            publicKeyPath = Files.createTempFile("jwt-public", ".pem");
            String publicPem = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()) +
                    "\n-----END PUBLIC KEY-----\n";
            Files.writeString(publicKeyPath, publicPem, StandardCharsets.UTF_8);

            Map<String, String> config = new HashMap<>();
            config.put("smallrye.jwt.sign.key-location", privateKeyPath.toUri().toString());
            config.put("mp.jwt.verify.publickey.location", publicKeyPath.toUri().toString());
            config.put("mp.jwt.verify.issuer", "test-issuer");

            return config;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JWT test keys", e);
        }
    }

    @Override
    public void stop() {
        try {
            if (privateKeyPath != null) Files.deleteIfExists(privateKeyPath);
            if (publicKeyPath != null) Files.deleteIfExists(publicKeyPath);
        } catch (Exception ignored) {}
    }
}
