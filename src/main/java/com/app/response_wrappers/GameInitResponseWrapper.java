package com.app.response_wrappers;

import com.app.models.BoardCell;
import com.app.models.RemainingShips;

import java.util.ArrayList;

public class GameInitResponseWrapper {
    private ArrayList<BoardCell> playerBoard;
    private ArrayList<BoardCell> enemyBoard;
    private RemainingShips playerShips;
    private RemainingShips enemyShips;
    private String enemyName;
    private boolean myTurn;
    private int winner;

    public ArrayList<BoardCell> getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(ArrayList<BoardCell> playerBoard) {
        this.playerBoard = playerBoard;
    }

    public ArrayList<BoardCell> getEnemyBoard() {
        return enemyBoard;
    }

    public void setEnemyBoard(ArrayList<BoardCell> enemyBoard) {
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
