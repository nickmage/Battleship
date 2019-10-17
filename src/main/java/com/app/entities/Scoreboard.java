package com.app.entities;

import com.auth.entities.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "scoreboard")
public class Scoreboard {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "order_id", unique = true)
    private UUID orderId;

    @OneToOne
    @JoinColumn(name="user_id")
    @MapsId
    private User user;

    @Column(name="wins", nullable=false)
    private Integer wins;

    @Column(name="loses", nullable=false)
    private Integer loses;

    public Scoreboard(/*Long id,*/ UUID  orderId, User user, Integer wins, Integer loses) {
        this. orderId =  orderId;
        //this.id = id;
        this.user = user;
        this.wins = wins;
        this.loses = loses;
    }

    public Scoreboard() {
    }

    /*public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }*/

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public UUID getOrderId() {
        return  orderId;
    }

    public void setOrderId(UUID userId) {
        this. orderId = userId;
    }
}