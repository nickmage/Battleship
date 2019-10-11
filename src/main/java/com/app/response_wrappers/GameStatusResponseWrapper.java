package com.app.response_wrappers;

import com.app.models.BoardCell;
import com.app.models.RemainingShips;

import java.util.ArrayList;

public class GameStatusResponseWrapper {

    private ArrayList<BoardCell> playerBoard;
    private RemainingShips playerShips;
    private boolean myTurn;
    private int winner;

    public ArrayList<BoardCell> getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(ArrayList<BoardCell> playerBoard) {
        this.playerBoard = playerBoard;
    }

    public RemainingShips getPlayerShips() {
        return playerShips;
    }

    public void setPlayerShips(RemainingShips playerShips) {
        this.playerShips = playerShips;
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
