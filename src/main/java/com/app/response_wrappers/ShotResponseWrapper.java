package com.app.response_wrappers;

import com.app.entities.BoardCell;
import com.app.entities.RemainingShips;

import java.util.ArrayList;

public class ShotResponseWrapper {
    private ArrayList<BoardCell> interactedCells;
    private RemainingShips remainingEnemyShips;
    private boolean myTurn;
    private int winner;

    public ArrayList<BoardCell> getInteractedCells() {
        return interactedCells;
    }

    public void setInteractedCells(ArrayList<BoardCell> interactedCells) {
        this.interactedCells = interactedCells;
    }

    public RemainingShips getRemainingEnemyShips() {
        return remainingEnemyShips;
    }

    public void setRemainingEnemyShips(RemainingShips remainingEnemyShips) {
        this.remainingEnemyShips = remainingEnemyShips;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

}
