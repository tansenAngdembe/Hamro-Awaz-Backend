package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="authority_access_groups_role_map")
public class AuthorityAccessGroupRoleMap  extends AbstractEntity{

    @ManyToOne(optional=false)
    @JoinColumn(name="authority_access_groups_id",referencedColumnName = "id")
    private AuthorityAccessGroup authorityAccessGroup;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(optional = false)
    @JoinColumn(name="authority_user_roles_id",referencedColumnName = "id")
    private AuthorityUserRole authorityUserRole;
}
