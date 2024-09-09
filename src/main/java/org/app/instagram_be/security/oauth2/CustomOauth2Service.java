package org.app.instagram_be.security.oauth2;

import org.app.instagram_be.model.entities.Account;
import org.app.instagram_be.model.entities.User;
import org.app.instagram_be.model.entities.enums.UserRoleEnum;
import org.app.instagram_be.repository.AccountRepository;
import org.app.instagram_be.repository.UserRepository;
import org.app.instagram_be.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOauth2Service extends DefaultOAuth2UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomOauth2Service.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    private Account accountDefault = new Account("noname", "12345", null);

    public CustomOauth2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        LOGGER.info("Loading user: {}", oAuth2User);
        try {
            return processOauth2User(oAuth2User, userRequest);
        } catch (OAuth2AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOauth2User(OAuth2User oAuth2User, OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(request.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (userInfo.getEmail().isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from provider");
        }
        User user = new User();
        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
        if (userOptional.isPresent()) {
            user = userOptional.get();
            LOGGER.info("Found user: {}", user);
            if (!user.getAuthProvider().equals(AuthProvider.valueOf(request.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationException("Looks like you're signed up with " +
                        user.getAuthProvider() + " account. Please use your " + user.getAuthProvider() +
                        " account to login.");
            }
            user = updateExistUser(user, userInfo);
        } else {
            user = registerNewUser(request, userInfo);
        }
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User updateExistUser(User user, OAuth2UserInfo oAuth2User) {
        user.setFullName(oAuth2User.getName());
        user.setImageUrl(oAuth2User.getImageUrl());
        return userRepository.save(user);
    }

    private User registerNewUser(OAuth2UserRequest request, OAuth2UserInfo userInfo) {
        User user = new User();
        user.setFullName(userInfo.getName());
        user.setProviderId(userInfo.getId());
        user.setEmail(userInfo.getEmail());
        user.setAuthProvider(AuthProvider.valueOf(request.getClientRegistration().getRegistrationId()));
        user.setImageUrl(userInfo.getImageUrl());
        user.setRole(UserRoleEnum.USER);
        user.setAccount(accountDefault);
        user = userRepository.save(user);
        accountDefault.setUser(user);
        accountRepository.save(accountDefault);
        return user;
    }
}
