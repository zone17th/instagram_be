package org.app.instagram_be.service.serviceImpl;

import org.app.instagram_be.model.entities.User;
import org.app.instagram_be.repository.UserRepository;
import org.app.instagram_be.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByEmailOrPhoneNumber(String userInput) {
        Optional<User> user = Optional.empty();
        user = userRepository.findByEmail(userInput);
        if (user.isEmpty()) {
            user = userRepository.findByPhoneNumber(userInput);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException(userInput);
            }
        }
        return user;
    }

    @Override
    public Optional<User> findByID(long id) {
        return userRepository.findUserById(id);
    }
}
