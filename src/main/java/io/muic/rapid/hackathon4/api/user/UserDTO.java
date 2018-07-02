package io.muic.rapid.hackathon4.api.user;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
public class UserDTO {

    private final Long id;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final Set<UserRole> roles;
    private final LoginMode loginMode;
    private final String facebookId;

    public UserDTO(@NotNull UserEntity entity, LoginMode loginMode){
        this.id = entity.getId();
        this.firstname = entity.getFirstname();
        this.lastname = entity.getLastname();
        this.roles = entity.getRoles();
        this.loginMode = loginMode;
        this.username = entity.getUsername();
        this.facebookId = entity.getFacebookId();
    }

    public UserDTO(@NotNull UserEntity entity){
        this(entity, null);
    }

    public UserDTO(@NotNull UserDTO dto, @NotNull UserEntity entity){


        this.loginMode = dto.getLoginMode();
        this.id = dto.getId();

        if (dto.username == null || !entity.getUsername().equals(dto.getUsername())){
            this.username = entity.getUsername();
        } else {
            this.username = dto.getUsername();
        }

        if (dto.getFacebookId() == null || !entity.getFacebookId().equals(dto.facebookId)){
            this.facebookId = entity.getFacebookId();
        } else {
            this.facebookId = dto.getFacebookId();
        }

        if (!entity.getFirstname().equals(dto.getFirstname())){
            this.firstname = entity.getFirstname();
        } else {
            this.firstname = dto.getFirstname();
        }

        if (!entity.getLastname().equals(dto.getLastname())){
            this.lastname = entity.getLastname();
        } else {
            this.lastname = dto.getLastname();
        }

        if (!entity.getRoles().equals(dto.getRoles())){
            this.roles = entity.getRoles();
        } else {
            this.roles = dto.getRoles();
        }
    }
}
