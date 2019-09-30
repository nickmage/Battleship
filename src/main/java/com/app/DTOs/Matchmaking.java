package com.app.DTOs;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "matchmaking")
@Component
public class Matchmaking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String player1Name;
    @Type(type = "uuid-char")
    private UUID player1Id;
    private String player2Name;
    @Type(type = "uuid-char")
    private UUID player2Id;
    @Type(type = "uuid-char")
    private UUID roomId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private String player1BoardJSON;
    private String player2BoardJSON;
    private String shipsOfPlayer1JSON;
    private String shipsOfPlayer2JSON;

    public Matchmaking(Long id, String player1Name, UUID player1Id, String player2Name, UUID player2Id,
                       UUID roomId, Date date, String player1BoardJSON, String player2BoardJSON,
                       String shipsOfPlayer1JSON, String shipsOfPlayer2JSON) {
        this.id = id;
        this.player1Name = player1Name;
        this.player1Id = player1Id;
        this.player2Name = player2Name;
        this.player2Id = player2Id;
        this.roomId = roomId;
        this.date = date;
        this.player1BoardJSON = player1BoardJSON;
        this.player2BoardJSON = player2BoardJSON;
        this.shipsOfPlayer1JSON = shipsOfPlayer1JSON;
        this.shipsOfPlayer2JSON = shipsOfPlayer2JSON;
    }

    public Matchmaking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public UUID getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(UUID player1Id) {
        this.player1Id = player1Id;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public UUID getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(UUID player2Id) {
        this.player2Id = player2Id;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlayer1BoardJSON() {
        return player1BoardJSON;
    }

    public void setPlayer1BoardJSON(String player1BoardJSON) {
        this.player1BoardJSON = player1BoardJSON;
    }

    public String getPlayer2BoardJSON() {
        return player2BoardJSON;
    }

    public void setPlayer2BoardJSON(String player2BoardJSON) {
        this.player2BoardJSON = player2BoardJSON;
    }

    public String getShipsOfPlayer1JSON() {
        return shipsOfPlayer1JSON;
    }

    public void setShipsOfPlayer1JSON(String shipsOfPlayer1JSON) {
        this.shipsOfPlayer1JSON = shipsOfPlayer1JSON;
    }

    public String getShipsOfPlayer2JSON() {
        return shipsOfPlayer2JSON;
    }

    public void setShipsOfPlayer2JSON(String shipsOfPlayer2JSON) {
        this.shipsOfPlayer2JSON = shipsOfPlayer2JSON;
    }
}
