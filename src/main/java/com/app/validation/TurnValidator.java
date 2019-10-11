package com.app.validation;

import com.app.cache.Room;
import com.app.models.BoardCell;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TurnValidator {
    @Autowired
    private ObjectMapper objectMapper;

    public boolean isValidTurn(Room room, int x, int y, String playerId) {
        return (x >= 0 && x < 10) && (y >= 0 && y < 10)
                && isPlayerAbleToTurn(room, playerId) && !isCellWasShotBefore(room, x, y);
    }

    private boolean isPlayerAbleToTurn(Room room, String playerId) {
        return (room.getCurrentPlayer() == 1 && playerId.equals(room.getPlayer1Id().toString()))
                || (room.getCurrentPlayer() == 2 && playerId.equals(room.getPlayer2Id().toString()));
    }

    private boolean isCellWasShotBefore(Room room, int x, int y) {
        if (room.getCurrentPlayer() == 1) {
            return checkCells(room.getEnemyBoardForPlayer1(), x, y);
        } else {
            return checkCells(room.getEnemyBoardForPlayer2(), x, y);
        }
    }

    private boolean checkCells(ArrayList<BoardCell> enemyBoard, int x, int y) {
        for (BoardCell cell : enemyBoard) {
            if (x == cell.getX() && y == cell.getY()) {
                return cell.getValue() <= 0;
            }
        }
        return false;
    }
}