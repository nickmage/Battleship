package com.app.DTOs;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "shot")
@Component
public class ShotDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Type(type = "uuid-char")
    private UUID roomId;
    @Type(type = "uuid-char")
    private UUID player1Id;
    @Type(type = "uuid-char")
    private UUID player2Id;
    private Character x;
    private Character y;
    private Integer value;

    public ShotDTO(Long id, UUID roomId, UUID player1Id, UUID player2Id, Character x, Character y, Integer value) {
        this.id = id;
        this.roomId = roomId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public ShotDTO() {
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

    public Character getX() {
        return x;
    }

    public void setX(Character x) {
        this.x = x;
    }

    public Character getY() {
        return y;
    }

    public void setY(Character y) {
        this.y = y;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
