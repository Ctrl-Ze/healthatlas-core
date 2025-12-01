package com.healthatlas.core.common.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.UUID;

@ApplicationScoped
public class JwtUserUtil {

    public UUID getCurrentUserUuid(SecurityContext ctx) {
        JsonWebToken jwt = (JsonWebToken) ctx.getUserPrincipal();
        return UUID.fromString(jwt.getClaim("user_id"));
    }
}

