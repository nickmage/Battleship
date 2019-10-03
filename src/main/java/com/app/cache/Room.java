package com.app.cache;

import com.app.entities.BoardCell;
import com.app.entities.RemainingShips;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class Room {

    private Integer currentPlayer;

    private String player1Name;
    private UUID player1Id;

    private String player2Name;
    private UUID player2Id;

    private HashSet<BoardCell> player1Board;
    private HashSet<BoardCell> enemyBoardForPlayer1 = new HashSet<>();
    private HashSet<BoardCell> player2Board;
    private HashSet<BoardCell> enemyBoardForPlayer2 = new HashSet<>();;

    private ArrayList<ArrayList<BoardCell>> shipsOfPlayer1;
    private ArrayList<ArrayList<BoardCell>> shipsOfPlayer2;

    private RemainingShips remainingShipsOfPlayer1 = new RemainingShips();
    private RemainingShips remainingShipsOfPlayer2 = new RemainingShips();

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

    public HashSet<BoardCell> getPlayer1Board() {
        return player1Board;
    }

    public void setPlayer1Board(HashSet<BoardCell> player1Board) {
        this.player1Board = player1Board;
    }

    public HashSet<BoardCell> getEnemyBoardForPlayer1() {
        return enemyBoardForPlayer1;
    }

    public void setEnemyBoardForPlayer1(HashSet<BoardCell> enemyBoardForPlayer1) {
        this.enemyBoardForPlayer1 = enemyBoardForPlayer1;
    }

    public HashSet<BoardCell> getPlayer2Board() {
        return player2Board;
    }

    public void setPlayer2Board(HashSet<BoardCell> player2Board) {
        this.player2Board = player2Board;
    }

    public HashSet<BoardCell> getEnemyBoardForPlayer2() {
        return enemyBoardForPlayer2;
    }

    public void setEnemyBoardForPlayer2(HashSet<BoardCell> enemyBoardForPlayer2) {
        this.enemyBoardForPlayer2 = enemyBoardForPlayer2;
    }

    public ArrayList<ArrayList<BoardCell>> getShipsOfPlayer1() {
        return shipsOfPlayer1;
    }

    public void setShipsOfPlayer1(ArrayList<ArrayList<BoardCell>> shipsOfPlayer1) {
        this.shipsOfPlayer1 = shipsOfPlayer1;
    }

    public ArrayList<ArrayList<BoardCell>> getShipsOfPlayer2() {
        return shipsOfPlayer2;
    }

    public void setShipsOfPlayer2(ArrayList<ArrayList<BoardCell>> shipsOfPlayer2) {
        this.shipsOfPlayer2 = shipsOfPlayer2;
    }

    public RemainingShips getRemainingShipsOfPlayer1() {
        return remainingShipsOfPlayer1;
    }

    public void setRemainingShipsOfPlayer1(RemainingShips remainingShipsOfPlayer1) {
        this.remainingShipsOfPlayer1 = remainingShipsOfPlayer1;
    }

    public RemainingShips getRemainingShipsOfPlayer2() {
        return remainingShipsOfPlayer2;
    }

    public void setRemainingShipsOfPlayer2(RemainingShips remainingShipsOfPlayer2) {
        this.remainingShipsOfPlayer2 = remainingShipsOfPlayer2;
    }

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "Room{" +
                "currentPlayer=" + currentPlayer +
                ", player1Name='" + player1Name + '\'' +
                ", player1Id=" + player1Id +
                ", player2Name='" + player2Name + '\'' +
                ", player2Id=" + player2Id +
                ", player1Board=" + player1Board +
                ", enemyBoardForPlayer1=" + enemyBoardForPlayer1 +
                ", player2Board=" + player2Board +
                ", enemyBoardForPlayer2=" + enemyBoardForPlayer2 +
                ", shipsOfPlayer1=" + shipsOfPlayer1 +
                ", shipsOfPlayer2=" + shipsOfPlayer2 +
                ", remainingShipsOfPlayer1=" + remainingShipsOfPlayer1 +
                ", remainingShipsOfPlayer2=" + remainingShipsOfPlayer2 +
                ", winner=" + winner +
                '}';
    }
}
