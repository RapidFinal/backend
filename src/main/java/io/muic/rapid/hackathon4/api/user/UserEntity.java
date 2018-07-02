package io.muic.rapid.hackathon4.api.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muic.rapid.hackathon4.api.common.AbstractRelationalEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor()
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "facebookId")
}, indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "facebookId"),
        @Index(columnList = "email")
})
public class UserEntity extends AbstractRelationalEntity {

    public UserEntity(UserDTO dto) {
        this.setId(dto.getId());
    }

    private String username;

    @JsonIgnore
    private String password;
    private String facebookId;
    private String firstname;
    private String lastname;
    private String email;

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "join_user_role", joinColumns = @JoinColumn(name = "user_id"))
    private Set<UserRole> roles = new HashSet<>();


    public boolean addRole(UserRole role){
        return this.roles.add(role);
    }

    public boolean addAllRole(Collection<UserRole> roles){
        return this.roles.addAll(roles);
    }

    public boolean removeRole(UserRole role){
        return this.roles.remove(role);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
