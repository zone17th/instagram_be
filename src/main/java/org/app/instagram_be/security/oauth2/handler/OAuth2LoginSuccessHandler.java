package org.app.instagram_be.security.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.app.instagram_be.security.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Configuration
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    @Autowired
    private TokenProvider tokenProvider;
    private static final String TARGET_URL= "http://localhost:3000/authorize";

    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = tokenProvider.generateAccessToken(authentication);
        LOGGER.info("token: {}", token);
        String redirectUri = UriComponentsBuilder.fromUriString(TARGET_URL).queryParam("accessToken", token).build().toString();
        LOGGER.info("redirectUri: {}", redirectUri);
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

}
