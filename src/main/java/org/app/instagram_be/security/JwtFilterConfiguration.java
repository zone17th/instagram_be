package org.app.instagram_be.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtFilterConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;

    public JwtFilterConfiguration(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(tokenProvider);
        builder.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
