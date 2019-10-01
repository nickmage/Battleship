package com.app.response_wrappers;

import com.app.entities.BoardCell;
import com.app.entities.RemainingShips;

public class ShotResponseWrapper {
    private BoardCell[] enemyBoard;
    private RemainingShips enemyShips;
    private boolean myTurn;
    private int winner;

    public BoardCell[] getEnemyBoard() {
        return enemyBoard;
    }

    public void setEnemyBoard(BoardCell[] enemyBoard) {
        this.enemyBoard = enemyBoard;
    }

    public RemainingShips getEnemyShips() {
        return enemyShips;
    }

    public void setEnemyShips(RemainingShips enemyShips) {
        this.enemyShips = enemyShips;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public int isWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

}
