package com.app.DTOs;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ready_check")
public class ReadyCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Type(type = "uuid-char")
    private UUID roomId;
    private boolean isPlayer1StartedGame;
    private boolean isPlayer2StartedGame;

    public ReadyCheck(Long id, UUID roomId, boolean isPlayer1StartedGame, boolean isPlayer2StartedGame) {
        this.id = id;
        this.roomId = roomId;
        this.isPlayer1StartedGame = isPlayer1StartedGame;
        this.isPlayer2StartedGame = isPlayer2StartedGame;
    }

    public ReadyCheck() {
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

    public boolean isPlayer1StartedGame() {
        return isPlayer1StartedGame;
    }

    public void setPlayer1StartedGame(boolean player1StartedGame) {
        isPlayer1StartedGame = player1StartedGame;
    }

    public boolean isPlayer2StartedGame() {
        return isPlayer2StartedGame;
    }

    public void setPlayer2StartedGame(boolean player2StartedGame) {
        isPlayer2StartedGame = player2StartedGame;
    }
}
