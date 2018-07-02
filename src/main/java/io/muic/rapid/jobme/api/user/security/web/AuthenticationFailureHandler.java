package io.muic.rapid.jobme.api.user.security.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LoginFailure loginFailure = new LoginFailure(exception.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(loginFailure.toString(), response.getWriter());
    }


    @Getter
    private static class LoginFailure {
        private int status;
        private boolean login;
        private String message;

        public LoginFailure(String message){
            this(HttpServletResponse.SC_UNAUTHORIZED, false, message);
        }

        private LoginFailure(int status, boolean login, String message) {
            this.status = status;
            this.login = login;
            this.message = message;
        }

        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writer().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
    }
}

