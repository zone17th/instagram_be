package org.app.instagram_be.service.serviceImpl;

import org.app.instagram_be.mappers.UserMapper;
import org.app.instagram_be.model.dto.LoginDTO;
import org.app.instagram_be.model.dto.LoginResponseDTO;
import org.app.instagram_be.model.entities.Account;
import org.app.instagram_be.model.entities.User;
import org.app.instagram_be.repository.AccountRepository;
import org.app.instagram_be.repository.UserRepository;
import org.app.instagram_be.security.TokenProvider;
import org.app.instagram_be.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    public AuthenticationServiceImpl(AccountRepository accountRepository, UserRepository userRepository, AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Account> getAccountByUserInput(String userInput) {
        Optional<Account> account = accountRepository.findAccountByUserInput(userInput);
        if (account.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return account;
    }

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUserInput(), loginDTO.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // get accesstoken after authenticate
            String accessToken = tokenProvider.generateAccessToken(authentication);
            // get user
            Optional<User> user = userRepository.findByEmailOrPhoneNumber(loginDTO.getUserInput());
            if (user.isPresent()) {
                LoginResponseDTO loginResponseDTO = userMapper.toLoginResponseDTO(user.get());
                loginResponseDTO.setAccessToken(accessToken);
                return loginResponseDTO;
            }
        } catch (Exception e) {
            LOGGER.error("Error while logging in", e);
            return null;
        }
        return null;
    }
}
