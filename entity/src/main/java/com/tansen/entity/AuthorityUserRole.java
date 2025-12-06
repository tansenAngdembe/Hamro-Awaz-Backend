package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "authority_user_roles")
public class AuthorityUserRole  extends AbstractEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "icon", nullable = true)
    private String icon;

    @Column(name = "navigation")
    private String navigation;

    @Column(name = "position")
    private Integer position;

    @Column(name = "ui_group_name")
    private String uiGroupName;

    @JoinColumn(name = "parent_role",referencedColumnName = "id")
    @ManyToOne
    private AdminRole parentRole;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "permission")
    private String permission;
}
