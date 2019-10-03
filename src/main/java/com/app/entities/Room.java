package com.app.entities;

import java.util.Date;
import java.util.UUID;

public class Room {
    private UUID roomId;

    private Integer currentPlayer;

    private String player1Name;
    private UUID player1Id;

    private String player2Name;
    private UUID player2Id;
    /*use objects instead of string
     * Dont do snapshots*/

    private String player1BoardJSON;
    private String enemyBoardForPlayer1JSON;
    private String player2BoardJSON;
    private String enemyBoardForPlayer2JSON;
    private String shipsOfPlayer1JSON;
    private String shipsOfPlayer2JSON;
    private String remainingShipsOfPlayer1JSON;
    private String remainingShipsOfPlayer2JSON;

    private Date date;

    private Integer winner;

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

    public String getPlayer1BoardJSON() {
        return player1BoardJSON;
    }

    public void setPlayer1BoardJSON(String player1BoardJSON) {
        this.player1BoardJSON = player1BoardJSON;
    }

    public String getEnemyBoardForPlayer1JSON() {
        return enemyBoardForPlayer1JSON;
    }

    public void setEnemyBoardForPlayer1JSON(String enemyBoardForPlayer1JSON) {
        this.enemyBoardForPlayer1JSON = enemyBoardForPlayer1JSON;
    }

    public String getPlayer2BoardJSON() {
        return player2BoardJSON;
    }

    public void setPlayer2BoardJSON(String player2BoardJSON) {
        this.player2BoardJSON = player2BoardJSON;
    }

    public String getEnemyBoardForPlayer2JSON() {
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
    }

    public String getRemainingShipsOfPlayer1JSON() {
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
    }

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
