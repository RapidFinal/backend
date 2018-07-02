package io.muic.rapid.hackathon4.api.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractRelationalEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Date createdDate;

    @NotNull
    private Date lastModifiedDate;

    @PrePersist
    public void onCreate(){
        createdDate = new Date();
        lastModifiedDate = createdDate;
    }

    @PreUpdate
    public void onUpdate(){
        lastModifiedDate = new Date();
    }

    @Override
    public abstract String toString();
}
