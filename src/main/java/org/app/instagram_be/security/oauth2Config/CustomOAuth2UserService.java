package org.app.instagram_be.security.oauth2Config;

import io.micrometer.common.util.StringUtils;
import org.app.instagram_be.model.entities.User;
import org.app.instagram_be.repository.UserRepository;
import org.app.instagram_be.security.oauth2Config.user.OAuth2UserInfo;
import org.app.instagram_be.security.oauth2Config.user.OAuth2UserInfoFactory;
import org.app.instagram_be.security.oauth2Config.user.UserPrincipal;
import org.app.instagram_be.security.oauth2Config.user.enums.AuthProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(oAuth2User, userRequest);
        } catch (Exception e) {
            LOGGER.error("Error while loading user", e);
            throw new UsernameNotFoundException("Error while loading user", e);
        }
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User, OAuth2UserRequest oAuth2UserRequest) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new UsernameNotFoundException("Email not found from OAuth2 provider");
        }
        Optional<User> userOpt = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user = new User();
        if(userOpt.isPresent()) {
            user = userOpt.get();
            if (!user.getAuthProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new UsernameNotFoundException("Look like you're signed up with " + user.getAuthProvider() + " account. Please " +
                        "use your " + user.getAuthProvider() + " account to login.");
            }
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        } else {
            user = updateExistingUser(user, oAuth2UserInfo);
        }
        return UserPrincipal.create(user);
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setAuthProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setFullName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());

        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFullName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
