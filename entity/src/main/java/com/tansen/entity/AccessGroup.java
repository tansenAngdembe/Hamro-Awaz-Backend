package com.tansen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="access_groups")
public class AccessGroup extends AbstractEntity{
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name="description")
    private String description;

    @JoinColumn(name="status",referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Status status;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="is_super_admin_group",nullable = false)
    private boolean isSuperAdminGroup;

    @Column(name="remarks")
    private String remarks;

    @OneToMany(mappedBy = "accessGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AccessGroupRoleMap> accessGroupRoleMaps;
}
