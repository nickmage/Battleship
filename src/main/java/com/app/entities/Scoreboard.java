package com.app.entities;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "scoreboard")
@Component
public class Scoreboard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private Integer wins;
    private Integer loses;

    public Scoreboard(Long id, String username, Integer wins, Integer loses) {
        this.id = id;
        this.username = username;
        this.wins = wins;
        this.loses = loses;
    }

    public Scoreboard() {
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

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLoses() {
        return loses;
    }

    public void setLoses(Integer loses) {
        this.loses = loses;
    }
}