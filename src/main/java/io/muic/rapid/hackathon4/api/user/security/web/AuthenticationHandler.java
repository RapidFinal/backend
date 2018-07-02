package io.muic.rapid.hackathon4.api.user.security.web;

import io.muic.rapid.hackathon4.api.common.exception.UnauthenticatedAccessException;
import io.muic.rapid.hackathon4.api.user.UserDTO;
import io.muic.rapid.hackathon4.api.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHandler implements AuthenticationProvider {

    private final UserService userService;


    @Autowired
    public AuthenticationHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (StringUtils.isAnyBlank(username, password)){
            throw new UnauthenticatedAccessException("Username or Password must not be bla");
        }

        UserDTO dto;
        if ((dto = userService.authenticateWebUser(username, password)) != null){
            return new UsernamePasswordAuthenticationToken(dto, null, dto.getRoles());
        }

        throw new UnauthenticatedAccessException("Invalid Credentials");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
