package com.unpa.sgpi.sgpi_backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenProviderTest {

    @Test
    void shouldGenerateAndValidateTokenWithPlainTextSecret() {
        JwtTokenProvider provider = new JwtTokenProvider(
                "sgpi-unpa-secret-key-que-debe-cambiarse-en-produccion-2026",
                3_600_000
        );
        UserDetails user = new User(
                "admin",
                "encoded-password",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String token = provider.generateToken(user);

        assertTrue(provider.isTokenValid(token));
        assertEquals("admin", provider.extractUsername(token));
        assertEquals(List.of("ADMIN"), provider.extractRoles(token));
        assertFalse(token.isBlank());
    }
}
