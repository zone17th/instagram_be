package org.app.instagram_be.controller;

import jakarta.validation.Valid;
import org.app.instagram_be.model.dto.LoginDTO;
import org.app.instagram_be.model.entities.Account;
import org.app.instagram_be.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        Optional<Account> account = authenticationService.getAccountByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Can not get account");
        }
        return ResponseEntity.ok(loginDTO);
    }
}
