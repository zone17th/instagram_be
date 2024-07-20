package org.app.instagram_be.service;

import org.app.instagram_be.model.dto.LoginDTO;
import org.app.instagram_be.model.dto.LoginResponseDTO;
import org.app.instagram_be.model.entities.Account;

import java.util.Optional;

public interface AuthenticationService {
    Optional<Account> getAccountByUserInput(String userInput);
    LoginResponseDTO login(LoginDTO loginDTO);
}
