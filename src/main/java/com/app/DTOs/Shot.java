package com.app.DTOs;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "shot")
@Component
public class Shot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Type(type = "uuid-char")
    private UUID roomId;
    @Type(type = "uuid-char")
    private UUID player1Id;
    @Type(type = "uuid-char")
    private UUID player2Id;
    private String shot;

    public Shot(Long id, UUID roomId, UUID player1Id, UUID player2Id, String shot) {
        this.id = id;
        this.roomId = roomId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.shot = shot;
    }

    public Shot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public UUID getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(UUID player1Id) {
        this.player1Id = player1Id;
    }

    public UUID getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(UUID player2Id) {
        this.player2Id = player2Id;
    }

    public String getShot() {
        return shot;
    }

    public void setShot(String shot) {
        this.shot = shot;
    }
}
