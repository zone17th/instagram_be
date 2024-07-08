package org.app.instagram_be.service.serviceImpl;

import org.app.instagram_be.model.entities.Account;
import org.app.instagram_be.repository.AccountRepository;
import org.app.instagram_be.service.AuthenticationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository accountRepository;

    public AuthenticationServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> getAccountByUserNameAndPassword(String username, String password) {
        return accountRepository.findByUserNameAndPassword(username, password);
    }
}
