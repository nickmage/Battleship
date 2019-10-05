package com.app.validation;

import com.app.DTOs.GameDTO;
import com.app.entities.BoardCell;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TurnValidator {
    @Autowired
    private ObjectMapper objectMapper;

    public boolean isValidTurn(GameDTO gameDTO, int x, int y, String playerId) throws IOException {
        return (x >= 0 && x < 10) && (y >= 0 && y < 10)
                && isPlayerAbleToTurn(gameDTO, playerId) && !isCellWasShotBefore(gameDTO, x, y);
    }

    private boolean isPlayerAbleToTurn(GameDTO gameDTO, String playerId) {
        return (gameDTO.getCurrentPlayer() == 1 && playerId.equals(gameDTO.getPlayer1Id().toString()))
                || (gameDTO.getCurrentPlayer() == 2 && playerId.equals(gameDTO.getPlayer2Id().toString()));
    }

    private boolean isCellWasShotBefore(GameDTO gameDTO, int x, int y) throws IOException {
        BoardCell[] enemyBoard;
        if (gameDTO.getCurrentPlayer() == 1) {
            enemyBoard = objectMapper.readValue(gameDTO.getPlayer2Ships(), BoardCell[].class);
        } else {
            enemyBoard = objectMapper.readValue(gameDTO.getPlayer1Ships(), BoardCell[].class);
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