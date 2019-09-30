package com.app.validation;

import com.app.DTOs.Game;
import com.app.entities.BoardCell;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TurnValidator {
    @Autowired
    private ObjectMapper objectMapper;

    public boolean isValidTurn(Game game, int x, int y, String playerId) throws IOException {
        return (x >= 0 && x < 10) && (y >= 0 && y < 10)
                && isPlayerAbleToTurn(game, playerId) && !isCellWasShotBefore(game, x, y);
    }

    private boolean isPlayerAbleToTurn(Game game, String playerId) {
        return (game.getCurrentPlayer() == 1 && playerId.equals(game.getPlayer1Id().toString()))
                || (game.getCurrentPlayer() == 2 && playerId.equals(game.getPlayer2Id().toString()));
    }

    private boolean isCellWasShotBefore(Game game, int x, int y) throws IOException {
        BoardCell[] enemyBoard;
        if (game.getCurrentPlayer() == 1) {
            enemyBoard = objectMapper.readValue(game.getPlayer2BoardJSON(), BoardCell[].class);
        } else {
            enemyBoard = objectMapper.readValue(game.getPlayer1BoardJSON(), BoardCell[].class);
        }
        return checkCells(enemyBoard, x,y);
    }

    private boolean checkCells(BoardCell[] enemyBoard, int x, int y) {
        for (BoardCell cell : enemyBoard) {
            if (x == cell.getX() && y == cell.getY()) {
                return cell.getValue() <= 0;
            }
        }
        return false;
    }
}