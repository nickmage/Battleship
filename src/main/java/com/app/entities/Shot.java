package com.app.entities;

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
    private UUID playerId;
    private Integer x;
    private Integer y;
    private Integer value;

    public Shot(Long id, UUID roomId, UUID playerId, Integer x, Integer y, Integer value) {
        this.id = id;
        this.roomId = roomId;
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.value = value;
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

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
