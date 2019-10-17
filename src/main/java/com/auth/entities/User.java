package com.auth.entities;

import com.app.entities.Scoreboard;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "account")
public class User {

    @Id
    @Column(name="id", unique = true)
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Scoreboard scoreboard;

    public User(String username, String password, UUID Id, Role role, Boolean enabled, Scoreboard scoreboard) {
        this.username = username;
        this.password = password;
        this.Id = Id;
        this.role = role;
        this.enabled = enabled;
        this.scoreboard = scoreboard;
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

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

}
