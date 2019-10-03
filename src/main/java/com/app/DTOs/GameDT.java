package com.app.DTOs;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "game")
@Component
public class GameDT {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Type(type = "uuid-char")
    private UUID roomId;
    private Integer currentPlayer;
    private Integer winner;

    private String player1Name;
    @Type(type = "uuid-char")
    private UUID player1Id;

    private String player2Name;
    @Type(type = "uuid-char")
    private UUID player2Id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Type(type="text")
    private String player1Board;
    @Type(type="text")
    private String player2Board;

    public GameDT(Long id, UUID roomId, Integer currentPlayer, String player1Name,
                  UUID player1Id, String player2Name, UUID player2Id, Date date,
                  String player1Board, Integer winner) {
        this.id = id;
        this.roomId = roomId;
        this.currentPlayer = currentPlayer;
        this.player1Name = player1Name;
        this.player1Id = player1Id;
        this.player2Name = player2Name;
        this.player2Id = player2Id;
        this.date = date;
        this.player1Board = player1Board;
        //this.enemyBoardForPlayer1JSON = enemyBoardForPlayer1JSON;
        this.player2Board = player2Board;
        //this.enemyBoardForPlayer2JSON = enemyBoardForPlayer2JSON;
        //this.shipsOfPlayer1JSON = shipsOfPlayer1JSON;
        //this.shipsOfPlayer2JSON = shipsOfPlayer2JSON;
        this.winner = winner;
        //this.remainingShipsOfPlayer1JSON = remainingShipsOfPlayer1JSON;
        //this.remainingShipsOfPlayer2JSON = remainingShipsOfPlayer2JSON;
    }

    public GameDT() {
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

    public Integer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Integer currentPlayer) {
        this.currentPlayer = currentPlayer;
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

    public String getPlayer1Board() {
        return player1Board;
    }

    public void setPlayer1Board(String player1Board) {
        this.player1Board = player1Board;
    }

    /*public String getEnemyBoardForPlayer1JSON() {
        return enemyBoardForPlayer1JSON;
    }

    public void setEnemyBoardForPlayer1JSON(String enemyBoardForPlayer1JSON) {
        this.enemyBoardForPlayer1JSON = enemyBoardForPlayer1JSON;
    }*/

    public String getPlayer2Board() {
        return player2Board;
    }

    public void setPlayer2Board(String player2Board) {
        this.player2Board = player2Board;
    }

    /*public String getEnemyBoardForPlayer2JSON() {
        return enemyBoardForPlayer2JSON;
    }

    public void setEnemyBoardForPlayer2JSON(String enemyBoardForPlayer2JSON) {
        this.enemyBoardForPlayer2JSON = enemyBoardForPlayer2JSON;
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
    }*/

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

    /*public String getRemainingShipsOfPlayer1JSON() {
        return remainingShipsOfPlayer1JSON;
    }

    public void setRemainingShipsOfPlayer1JSON(String remainingShipsOfPlayer1JSON) {
        this.remainingShipsOfPlayer1JSON = remainingShipsOfPlayer1JSON;
    }

    public String getRemainingShipsOfPlayer2JSON() {
        return remainingShipsOfPlayer2JSON;
    }

    public void setRemainingShipsOfPlayer2JSON(String remainingShipsOfPlayer2JSON) {
        this.remainingShipsOfPlayer2JSON = remainingShipsOfPlayer2JSON;
    }*/
}
