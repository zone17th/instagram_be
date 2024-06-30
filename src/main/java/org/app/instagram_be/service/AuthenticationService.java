package org.app.instagram_be.service;

import org.app.instagram_be.model.entities.Account;

import java.util.Optional;

public interface AuthenticationService {
    Optional<Account> getAccountByEmailAndPassword(String email, String password);
}
