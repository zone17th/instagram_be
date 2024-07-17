package org.app.instagram_be.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter extends GenericFilter {
    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String bearerToken = request.getHeader("Authorization");
        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        if (token!=null) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
