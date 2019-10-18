package com.app.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @Column(name="id", unique = true)
    @Type(type = "uuid-char")
    private UUID roomId;

    /*take from Accounts
     *
     **/
    @Column(name="player1name")
    private String player1Name;

    @Column(name="player1id")
    @Type(type = "uuid-char")
    private UUID player1Id;

    @Column(name="player2name")
    private String player2Name;

    @Column(name="player2id")
    @Type(type = "uuid-char")
    private UUID player2Id;

    @Column(name="date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name="player1ships")
    @Type(type = "text")
    private String player1Ships;

    @Column(name="player2ships")
    @Type(type = "text")
    private String player2Ships;

    @Column(name="type", nullable=false, length = 20)
    @Enumerated(EnumType.STRING)
    private GameType type;

    @Column(name="room_name")
    private String roomName;
    @Column(name="password")
    private String password;

    @Column(name="current_player")
    private Integer currentPlayer;

    @Column(name="winner")
    private Integer winner;

    public Game(UUID roomId, String player1Name, UUID player1Id, String player2Name, UUID player2Id, Date date,
                String player1Ships, String player2Ships, GameType type, String roomName, String password,
                Integer currentPlayer, Integer winner) {
        this.roomId = roomId;
        this.player1Name = player1Name;
        this.player1Id = player1Id;
        this.player2Name = player2Name;
        this.player2Id = player2Id;
        this.date = date;
        this.player1Ships = player1Ships;
        this.player2Ships = player2Ships;
        this.type = type;
        this.roomName = roomName;
        this.password = password;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
    }

    public Game() {
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
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

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Integer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

}
