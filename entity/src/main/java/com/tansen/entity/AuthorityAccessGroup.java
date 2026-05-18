package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name="authority_access_groups")
public class AuthorityAccessGroup extends AbstractEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status", referencedColumnName = "id")
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_authority_admin_group", nullable = false)
    private boolean isAuthorityAdminGroup;

    @Column(name = "remarks")
    private String remarks;

    @OneToMany(mappedBy = "authorityAccessGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AuthorityAccessGroupRoleMap> authorityAccessGroupRoleMaps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", nullable = false)
    private AdministrativeUnit municipality;
}

