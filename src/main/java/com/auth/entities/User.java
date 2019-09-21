package com.auth.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    @Type(type = "uuid-char")
    private UUID uuid;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    private Set<Role> role;

    public User(Long id, String username, String password, UUID uuid,Set<Role> role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.uuid = uuid;
        this.role = role;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
}
