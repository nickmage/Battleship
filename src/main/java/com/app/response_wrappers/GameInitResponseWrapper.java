package com.app.response_wrappers;

import com.app.entities.BoardCell;
import com.app.entities.RemainingShips;
import java.util.HashSet;

public class GameInitResponseWrapper {
    private HashSet<BoardCell> playerBoard;
    private HashSet<BoardCell> enemyBoard;
    private RemainingShips playerShips;
    private RemainingShips enemyShips;
    private String enemyName;
    private boolean myTurn;
    private int winner;

    public HashSet<BoardCell> getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(HashSet<BoardCell> playerBoard) {
        this.playerBoard = playerBoard;
    }

    public HashSet<BoardCell> getEnemyBoard() {
        return enemyBoard;
    }

    public void setEnemyBoard(HashSet<BoardCell> enemyBoard) {
        this.enemyBoard = enemyBoard;
    }

    public RemainingShips getPlayerShips() {
        return playerShips;
    }

    public void setPlayerShips(RemainingShips playerShips) {
        this.playerShips = playerShips;
    }

    public RemainingShips getEnemyShips() {
        return enemyShips;
    }

    public void setEnemyShips( RemainingShips enemyShips) {
        this.enemyShips =  enemyShips;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}
