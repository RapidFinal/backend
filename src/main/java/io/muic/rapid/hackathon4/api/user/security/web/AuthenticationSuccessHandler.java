package io.muic.rapid.hackathon4.api.user.security.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.getWriter().write(new LoginSuccess(response.getStatus(), authentication.isAuthenticated(), authentication.getAuthorities()).toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        clearAuthenticationAttributes(request);
    }

    @Getter
    static class LoginSuccess {
        private int status;
        private boolean login;
        private Collection<? extends GrantedAuthority> role;

        public LoginSuccess(int status, boolean login, Collection<? extends GrantedAuthority> role) {
            this.status = status;
            this.login = login;
            this.role = role;
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
