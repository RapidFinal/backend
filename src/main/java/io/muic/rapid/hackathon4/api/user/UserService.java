package io.muic.rapid.hackathon4.api.user;

import io.muic.rapid.hackathon4.api.common.exception.DuplicateEntryException;
import io.muic.rapid.hackathon4.api.common.exception.UnauthenticatedAccessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.muic.rapid.hackathon4.api.user.LoginMode.WEB;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bcrypt;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bcrypt) {
        this.userRepository = userRepository;
        this.bcrypt = bcrypt;
    }

    public UserDTO getUserDTO(Authentication auth) throws UnauthenticatedAccessException {
        Object principal = auth.getPrincipal();

        if (!(principal instanceof UserDTO)){
            throw new UnauthenticatedAccessException("Principal is null");
        }

        return (UserDTO) principal;
    }

    public UserDTO authenticateWebUser(String username, String password) throws UnauthenticatedAccessException {
        if (StringUtils.isAnyEmpty(username, password)){
            throw new UnauthenticatedAccessException("Username or Password is empty");
        }

        UserEntity entity = findEntityByUsername(username);

        if (entity == null || !bcrypt.matches(password, entity.getPassword())){
            throw new UnauthenticatedAccessException("Invalid Credentials");
        }

        return new UserDTO(entity, WEB);
    }

    //TODO: Implement facebook login
    public UserDTO authenticateFacebookUser(String token) {
        return null;
    }

    @Transactional
    public UserDTO createWebUser(WebRegisterDAO dao){
        List<UserRole> role = new ArrayList<UserRole>(){{
            add(UserRole.ROLE_USER);
        }};
        return createWebUser(dao.getUsername(), dao.getPassword(), dao.getRepeatpassword(), dao.getFirstname(), dao.getLastname(), dao.getEmail(), role);
    }

    private UserDTO createWebUser(
            @NotNull String username,
            @NotNull String password,
            @NotNull String repeatpassword,
            @NotNull String firstname,
            @NotNull String lastname,
            @NotNull String email,
            @NotNull Collection<UserRole> roles) throws DuplicateEntryException {

        if (usernameExist(username)){
            throw new DuplicateEntryException(String.format("username already exist: %s", username));
        }

        if (emailExists(email)){
            throw new DuplicateEntryException(String.format("email already exist: %s", email));
        }

        if (!password.contentEquals(repeatpassword)){
            throw new IllegalArgumentException("password and repeat password do not match");
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setFirstname(firstname);
        entity.setLastname(lastname);
        entity.setPassword(bcrypt.encode(password));
        entity.addAllRole(roles); // TODO: fix role
        entity.setUsername(username);

        entity = userRepository.save(entity);

        return new UserDTO(entity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    private UserDTO updateUserSession(UserDTO dto, UserEntity entity){
        // https://stackoverflow.com/questions/7889660/how-to-reload-spring-security-principal-after-updating-in-hibernate

        UserDTO dto1 = new UserDTO(dto, entity);
        PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken = new PreAuthenticatedAuthenticationToken(dto1, null,dto1.getRoles());
        SecurityContextHolder.getContext().setAuthentication(preAuthenticatedAuthenticationToken);

        return dto1;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO addRole(@NotNull UserDTO dto, @NotNull UserRole role){
        return addAllRole(dto, new ArrayList<UserRole>(1) {{ add(role); }});
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO addAllRole(@NotNull UserDTO dto, @NotEmpty @NotNull Collection<UserRole> roles){
        UserEntity entity = findEntityByDTO(dto);
        entity.addAllRole(roles);
        return updateUserSession(dto,  userRepository.save(entity)); // TODO: trigger security role change
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO removeRole(UserDTO dto, UserRole role){
        UserEntity entity = findEntityByDTO(dto);
        entity.removeRole(role);
        return updateUserSession(dto,  userRepository.save(entity));
    }

    @Transactional
    public UserDTO createFacebookUser(FacebookRegisterDAO dao){
        List<UserRole> role = new ArrayList<UserRole>(){{
            add(UserRole.ROLE_USER);
        }};
        return createFacebookUser(dao.getFacebookId(), dao.getFirstname(), dao.getLastname(), role);
    }

    private UserDTO createFacebookUser(
            @NotNull String facebookId,
            @NotNull String firstname,
            @NotNull String lastname,
            @NotNull List<UserRole> roles){

        if (facebookIdExist(facebookId)) {
            throw new DuplicateEntryException(String.format("facebook user already registered: %s", facebookId));
        }

        UserEntity entity = new UserEntity();
        entity.setFacebookId(facebookId);
        entity.setFirstname(firstname);
        entity.setLastname(lastname);
        entity.addAllRole(roles);

        return new UserDTO(userRepository.save(entity));
    }

    private boolean facebookIdExist(String facebookId){
        return userRepository.existsByFacebookId(facebookId);
    }

    private boolean usernameExist(String username){
        return userRepository.existsByUsername(username);
    }

    private boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }

    public UserDTO getUserByUsername(String username){
        return new UserDTO(findEntityByUsername(username));
    }

    public UserDTO getUserByFacebookId(String facebookId){
        return new UserDTO(findEntityByFacebookId(facebookId));
    }

    public UserDTO findByEmail(String email){
        return new UserDTO(findEntityByEmail(email));
    }

    private UserEntity findEntityByDTO(UserDTO userDTO){
        UserEntity entity = null;
        switch (userDTO.getLoginMode()){
            case WEB:
                entity = findEntityByUsername(userDTO.getUsername());
                break;
            case FACEBOOK:
                entity = findEntityByFacebookId(userDTO.getFacebookId());
                break;

            default:
                throw new NullPointerException("DTO Does not contain login mode!");
        }

        return entity;
    }

    private UserEntity findEntityByEmail(String email){
        return userRepository.findByEmail(email);
    }

    private UserEntity findEntityByFacebookId(String facebookId){
        return userRepository.findByFacebookId(facebookId);
    }

    private UserEntity findEntityByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
