package com.healthatlas.core.bloodtests.util;

import io.smallrye.jwt.build.Jwt;
import java.util.List;
import java.util.UUID;

public final class TestTokenFactory {

    private TestTokenFactory() {
    }

    public static String createToken(UUID userId, List<String> groups) {
        return Jwt.issuer("test-issuer")
                .upn("test@healthatlas.com")
                .claim("user_id", userId.toString())
                // Ensure TestKeyResource.PRIVATE_KEY_URI is available (static field access)
                .claim("groups", groups)
                .expiresIn(3600)
                .sign(TestKeyResource.PRIVATE_KEY_URI);
    }

    public static String createUserToken(UUID userId) {
        return createToken(userId, List.of("user"));
    }
}
