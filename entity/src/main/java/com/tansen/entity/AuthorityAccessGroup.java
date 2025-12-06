package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name="authority_access_groups")
public class AuthorityAccessGroup extends AbstractEntity {
    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description")
    private String description;
    @JoinColumn(name="status",referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Status status;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name="updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name="is_authority_admin_group",nullable = false)
    private boolean isVendorAdminGroup;

    @Column(name="remarks")
    private String remarks;

    @OneToMany(mappedBy = "authorityAccessGroup",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<AuthorityAccessGroupRoleMap> authorityAccessGroupRoleMaps;
}
