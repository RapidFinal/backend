package io.muic.rapid.hackathon4.api.common;

import io.muic.rapid.hackathon4.api.user.UserDTO;
import io.muic.rapid.hackathon4.api.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractRelationalAuditorEntity extends AbstractRelationalEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity createdBy;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity modifiedBy;

    @PrePersist
    public void onSave() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) modifiedBy = createdBy = null;
        else {
            modifiedBy = createdBy = new UserEntity(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        }
    }

    @PreUpdate
    public void onUpdate() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) modifiedBy = null;
        else {
            modifiedBy = new UserEntity(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        }
    }
}
