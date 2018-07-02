package io.muic.rapid.hackathon4.api.user.security.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class AuthenticationEntryHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        FailureLogin failureLogin = new FailureLogin(authException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(failureLogin.toString(), response.getWriter());
    }

    @Getter
    private static class FailureLogin {
        private int status;
        private boolean login;
        private String message;

        public FailureLogin(String message){
            this(HttpServletResponse.SC_UNAUTHORIZED, false, message);
        }

        private FailureLogin(int status, boolean login, String message) {
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