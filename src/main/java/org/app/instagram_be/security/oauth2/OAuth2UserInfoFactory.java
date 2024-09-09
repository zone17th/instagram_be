package org.app.instagram_be.security.oauth2;

import org.app.instagram_be.security.oauth2.user.FacebookOAuth2UserInfo;
import org.app.instagram_be.security.oauth2.user.GoogleOAuth2UserInfo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        }else {
            throw new UsernameNotFoundException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
