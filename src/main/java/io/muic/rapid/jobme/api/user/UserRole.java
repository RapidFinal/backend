package io.muic.rapid.jobme.api.user;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotNull;

@Getter
public enum UserRole implements GrantedAuthority {
    ROLE_ADMIN("I am the One"), ROLE_USER("I am everyone else");


    private String description;

    UserRole(@NotNull String description){
        this.description = description;
    }

    @Override
    @JsonValue
    public String toString() {
        return super.name();
    }

    @Override
    public String getAuthority() {
        return super.toString();
    }
}
