package org.app.instagram_be.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public class SecurityUtil {
    public static Optional<String> getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return Optional.of(((User) principal).getUsername());
        }
        return Optional.empty();
    }
}
