package org.app.instagram_be.service.serviceImpl;

import org.app.instagram_be.model.entities.Account;
import org.app.instagram_be.repository.AccountRepository;
import org.app.instagram_be.service.AuthenticationService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository accountRepository;

    public AuthenticationServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> getAccountByUserInputAndPassword(String userInput, String password) {
        Optional<Account> account = Optional.empty();
        account = accountRepository.findByUserNameAndPassword(userInput, password);
        if (account.isEmpty()) {
            account = accountRepository.findByUserEmailAndPassword(userInput, password);
            if (account.isEmpty()) {
                account = accountRepository.findByUserPhoneNumberAndPassword(userInput, password);
                if (account.isEmpty()) {
                    throw new UsernameNotFoundException("User not found");
                }
            }
        }
        return account;
    }
}
