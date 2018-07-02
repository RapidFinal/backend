package io.muic.rapid.hackathon4.api.user.security.facebook;

import io.muic.rapid.hackathon4.api.user.FacebookRegisterDAO;
import io.muic.rapid.hackathon4.api.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp {

    @Autowired
    private UserService userService;

    @Override
    public String execute(Connection<?> connection) {
        UserProfile profile = connection.fetchUserProfile();
        userService.createFacebookUser(new FacebookRegisterDAO(profile.getId(), profile.getFirstName(), profile.getLastName(), connection.getProfileUrl()));
        return profile.getId();
    }
}
