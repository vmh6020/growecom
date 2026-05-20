package com.kevin.growecom.model;


import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    private String createdBy;
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
//       this.createdBy = SecurityUtil.getCurrentUserLogin().orElse("Unknown");
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
//        this.updatedBy = SecurityUtil.getCurrentUserLogin().orElse("Unknown");
    }
}
