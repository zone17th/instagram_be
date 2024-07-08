package org.app.instagram_be.service;

import org.app.instagram_be.model.entities.Account;

import java.util.Optional;

public interface AuthenticationService {
    Optional<Account> getAccountByUserNameAndPassword(String email, String password);
}
