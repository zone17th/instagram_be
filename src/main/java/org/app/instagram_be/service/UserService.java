package org.app.instagram_be.service;

import org.app.instagram_be.model.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmailOrPhoneNumber(String userInput);
}
