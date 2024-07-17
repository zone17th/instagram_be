package org.app.instagram_be.service.serviceImpl;

import org.app.instagram_be.mappers.AccountMapper;
import org.app.instagram_be.model.dto.LoginDTO;
import org.app.instagram_be.model.dto.LoginResponseDTO;
import org.app.instagram_be.model.entities.Account;
import org.app.instagram_be.model.entities.User;
import org.app.instagram_be.repository.AccountRepository;
import org.app.instagram_be.repository.UserRepository;
import org.app.instagram_be.security.TokenProvider;
import org.app.instagram_be.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final AccountMapper accountMapper;

    public AuthenticationServiceImpl(AccountRepository accountRepository, UserRepository userRepository, AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.accountMapper = accountMapper;
    }

    @Override
    public Optional<Account> getAccountByUserInputAndPassword(String userInput, String password) {
        Optional<Account> account = accountRepository.findAccountByUserInputAndPassword(userInput, password);
        if (account.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return account;
    }

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUserInput(),loginDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get accesstoken after authenticate
        String accessToken = tokenProvider.generateAccessToken(authentication);
        // get user
        Optional<User> user = userRepository.findByEmailOrPhoneNumber(loginDTO.getUserInput());
        if (user.isPresent()) {
            LoginResponseDTO loginResponseDTO = accountMapper.toLoginResponseDTO(user.get());
            loginResponseDTO.setAccessToken(accessToken);
            return loginResponseDTO;
        }
        return null;
    }

}
