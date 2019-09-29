package com.app.response_wrappers;

import com.app.entities.BoardCell;
import com.app.entities.RemainingShips;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GameInitResponseWrapper {
    private BoardCell[] playerBoard;
    private BoardCell[] enemyBoard;
    private RemainingShips playerShips;
    private RemainingShips enemyShips;
    private String enemyName;
    private boolean myTurn;

    public BoardCell[] getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(String playerBoard) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.playerBoard = objectMapper.readValue(playerBoard, BoardCell[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BoardCell[] getEnemyBoard() {
        return enemyBoard;
    }

    public void setEnemyBoard(String enemyBoard) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.enemyBoard = objectMapper.readValue(enemyBoard, BoardCell[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RemainingShips getPlayerShips() {
        return playerShips;
    }

    public void setPlayerShips(String playerShips) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.playerShips = objectMapper.readValue(playerShips, RemainingShips.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RemainingShips getEnemyShips() {
        return enemyShips;
    }

    public void setEnemyShips(String enemyShips) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.enemyShips = objectMapper.readValue(enemyShips, RemainingShips.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
