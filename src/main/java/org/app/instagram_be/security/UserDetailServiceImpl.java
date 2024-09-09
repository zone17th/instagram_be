package org.app.instagram_be.security;

import org.app.instagram_be.model.entities.Account;
import org.app.instagram_be.repository.AccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class UserDetailServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    public UserDetailServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userInput) throws UsernameNotFoundException {
        Optional<Account> accountOpt = accountRepository.findAccountByUserInput(userInput);
        if (accountOpt.isEmpty()) {
            throw new UsernameNotFoundException(userInput);
        }
        Account account = accountOpt.get();
        return new User(
                userInput,
                account.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + account.getUser().getRole()))
        );
    }
}
