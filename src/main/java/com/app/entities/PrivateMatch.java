package com.app.entities;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "private_matchmaking")
@Component
public class PrivateMatch implements Matchmaking{
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
    @Type(type = "text")
    private String player1Ships;
    @Type(type = "text")
    private String player2Ships;
    private String roomName;
    private String password;

    public PrivateMatch(Long id, String player1Name, UUID player1Id, String player2Name, UUID player2Id, UUID roomId,
                        Date date, String player1Ships, String player2Ships, String roomName, String password) {
        this.id = id;
        this.player1Name = player1Name;
        this.player1Id = player1Id;
        this.player2Name = player2Name;
        this.player2Id = player2Id;
        this.roomId = roomId;
        this.date = date;
        this.player1Ships = player1Ships;
        this.player2Ships = player2Ships;
        this.roomName = roomName;
        this.password = password;
    }

    public PrivateMatch() {
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

    public String getPlayer1Ships() {
        return player1Ships;
    }

    public void setPlayer1Ships(String player1Ships) {
        this.player1Ships = player1Ships;
    }

    public String getPlayer2Ships() {
        return player2Ships;
    }

    public void setPlayer2Ships(String player2Ships) {
        this.player2Ships = player2Ships;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
