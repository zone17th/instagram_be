package org.app.instagram_be.controller;

import jakarta.validation.Valid;
import org.app.instagram_be.model.dto.LoginDTO;
import org.app.instagram_be.model.dto.LoginResponseDTO;
import org.app.instagram_be.model.entities.Account;
import org.app.instagram_be.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        Optional<Account> account = authenticationService.getAccountByUserInput(loginDTO.getUserInput());
        if (account.isPresent()) {
            LoginResponseDTO loginResponseDTO = authenticationService.login(loginDTO);
            if (loginResponseDTO!=null) {
                return ResponseEntity.ok(loginResponseDTO);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Can't login");
    }
}
