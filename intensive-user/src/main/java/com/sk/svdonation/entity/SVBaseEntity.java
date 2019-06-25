package com.sk.svdonation.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class SVBaseEntity {
    @Column(name = "CREATE_AT", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "LAST_MODIFY_AT", nullable = false)
    private LocalDateTime lastModifyAt;

    @PrePersist
    private void prePersist() {
        this.createAt = this.lastModifyAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.lastModifyAt = LocalDateTime.now();
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getLastModifyAt() {
        return lastModifyAt;
    }
}