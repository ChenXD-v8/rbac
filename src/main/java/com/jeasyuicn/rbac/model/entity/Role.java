package com.jeasyuicn.rbac.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="rbac_role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="role_name",nullable = false,length = 32,unique = true)
    private String roleName;
    @Column(name="role_Key",nullable = false,length = 32,unique = true)
    private String roleKey;
    private Boolean enable=false;
    private String description;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="rbac_role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @JsonIgnore
    private Set<Permission> permissions;
}
