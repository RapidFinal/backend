package io.muic.rapid.hackathon4.api.user.security.facebook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.SignInAdapter;

@Configuration
public class SocialConfiguration {

    @Bean
    public SignInAdapter authSignInAdapter() {
        return (userId, connection, request) -> {
            UserProfile profile = connection.fetchUserProfile();
            String facebookid = profile.getId();

            return null;
        };
    }
}
