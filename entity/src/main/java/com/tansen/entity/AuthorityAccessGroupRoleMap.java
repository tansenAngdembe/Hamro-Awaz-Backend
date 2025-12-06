package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="authority_access_groups_role_map")
public class AuthorityAccessGroupRoleMap  extends AbstractEntity{


    @JoinColumn(name="vendor_access_groups",referencedColumnName = "id")
    @ManyToOne(optional=false)
    private AuthorityAccessGroup authorityAccessGroup;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @JoinColumn(name="vendor_user_roles",referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AuthorityUserRole authorityUserRole;
}
