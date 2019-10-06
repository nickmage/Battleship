package com.app.cache;

import com.app.entities.BoardCell;

import java.util.ArrayList;
import java.util.UUID;

public class Room {

    private UUID roomId;

    private Integer currentPlayer;

    private String player1Name;
    private UUID player1Id;

    private String player2Name;
    private UUID player2Id;

    private ArrayList<BoardCell> player1Board;//player1Ships modified + shots of player2
    private ArrayList<BoardCell> enemyBoardForPlayer1 = new ArrayList<>();//shots of player1
    private ArrayList<BoardCell> player2Board;//player2Ships modified + shots of player1
    private ArrayList<BoardCell> enemyBoardForPlayer2 = new ArrayList<>();//shots of player2

    private ArrayList<ArrayList<BoardCell>> player1Ships;
    private ArrayList<ArrayList<BoardCell>> player2Ships;

    private Integer winner = 0;

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

    public ArrayList<BoardCell> getPlayer1Board() {
        return player1Board;
    }

    public void setPlayer1Board(ArrayList<BoardCell> player1Board) {
        this.player1Board = player1Board;
    }

    public ArrayList<BoardCell> getEnemyBoardForPlayer1() {
        return enemyBoardForPlayer1;
    }

    public void setEnemyBoardForPlayer1(ArrayList<BoardCell> enemyBoardForPlayer1) {
        this.enemyBoardForPlayer1 = enemyBoardForPlayer1;
    }

    public ArrayList<BoardCell> getPlayer2Board() {
        return player2Board;
    }

    public void setPlayer2Board(ArrayList<BoardCell> player2Board) {
        this.player2Board = player2Board;
    }

    public ArrayList<BoardCell> getEnemyBoardForPlayer2() {
        return enemyBoardForPlayer2;
    }

    public void setEnemyBoardForPlayer2(ArrayList<BoardCell> enemyBoardForPlayer2) {
        this.enemyBoardForPlayer2 = enemyBoardForPlayer2;
    }

    public ArrayList<ArrayList<BoardCell>> getPlayer1Ships() {
        return player1Ships;
    }

    public void setPlayer1Ships(ArrayList<ArrayList<BoardCell>> player1Ships) {
        this.player1Ships = player1Ships;
    }

    public ArrayList<ArrayList<BoardCell>> getPlayer2Ships() {
        return player2Ships;
    }

    public void setPlayer2Ships(ArrayList<ArrayList<BoardCell>> player2Ships) {
        this.player2Ships = player2Ships;
    }

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", currentPlayer=" + currentPlayer +
                ", player1Name='" + player1Name + '\'' +
                ", player1Id=" + player1Id +
                ", player2Name='" + player2Name + '\'' +
                ", player2Id=" + player2Id +
                ", player1Board=" + player1Board +
                ", enemyBoardForPlayer1=" + enemyBoardForPlayer1 +
                ", player2Board=" + player2Board +
                ", enemyBoardForPlayer2=" + enemyBoardForPlayer2 +
                ", shipsOfPlayer1=" + player1Ships +
                ", shipsOfPlayer2=" + player2Ships +
                ", winner=" + winner +
                '}';
    }
}
