package com.auth.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "account")
public class User {
    @Id
    @Column(name="id", nullable=false)
    @Type(type = "uuid-char")
    private UUID Id;

    @Column(name="username", nullable=false, length = 20)
    private String username;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="role", nullable=false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="enabled", nullable=false, length = 20)
    @Type(type = "yes_no")
    private Boolean enabled;

    public User(String username, String password, UUID Id, Role role, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.Id = Id;
        this.role = role;
        this.enabled = enabled;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        this.Id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
