package com.backend.auth.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.UUID;

public class JwtAuthentication extends AbstractAuthenticationToken {

    @Getter
    private final String userId;

    public JwtAuthentication(String userId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId = userId;
        super.setAuthenticated(true); // Must use super, as we validated the token
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userId;
    }
	
}

