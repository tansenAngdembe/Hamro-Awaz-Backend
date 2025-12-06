package com.tansen.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="access_groups_role_map")
public class AccessGroupRoleMap  extends AbstractEntity{
    @JoinColumn(name="access_groups",referencedColumnName = "id")
    @ManyToOne(optional=false)
    private AccessGroup accessGroup;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @JoinColumn(name="admin_roles",referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AdminRole adminRole;
}
