package com.app.services;

import com.app.cache.Room;
import com.app.exception.WinnerException;
import com.app.response_wrappers.GameInitResponseWrapper;
import org.springframework.stereotype.Service;


@Service
public class GameInitResponseCreator {

    private final RemainingShipsCreator remainingShips;

    public GameInitResponseCreator(RemainingShipsCreator remainingShips) {
        this.remainingShips = remainingShips;
    }

    public GameInitResponseWrapper getResponse(Room room, String playerId) {
        GameInitResponseWrapper response = new GameInitResponseWrapper();
        if (playerId.equals(room.getPlayer1Id().toString())) {
            response.setPlayerBoard(room.getPlayer1Board());
            try {
                response.setPlayerShips(remainingShips.getRemainingShips(room.getPlayer1Ships()));
            } catch (WinnerException e) {
                response.setWinner(-1);
                room.setWinner(-1);
            }
            try {
                response.setEnemyShips(remainingShips.getRemainingShips(room.getPlayer2Ships()));
            } catch (WinnerException e) {
                response.setWinner(1);
                room.setWinner(1);
            }
            if (room.getEnemyBoardForPlayer1() != null) {
                response.setEnemyBoard(room.getEnemyBoardForPlayer1());
            }
            response.setEnemyName(room.getPlayer2Name());
            response.setMyTurn(room.getCurrentPlayer() == 1);
        } else {
            response.setPlayerBoard(room.getPlayer2Board());
            try {
                response.setPlayerShips(remainingShips.getRemainingShips(room.getPlayer2Ships()));
            } catch (WinnerException e) {
                response.setWinner(-1);
                room.setWinner(-1);
            }
            try {
                response.setEnemyShips(remainingShips.getRemainingShips(room.getPlayer1Ships()));
            } catch (WinnerException e) {
                response.setWinner(1);
                room.setWinner(1);
            }
            if (room.getEnemyBoardForPlayer2() != null) {
                response.setEnemyBoard(room.getEnemyBoardForPlayer2());
            }
            response.setEnemyName(room.getPlayer1Name());
            response.setMyTurn(room.getCurrentPlayer() == 2);
        }
        response.setWinner(room.getWinner());
        return response;
    }
}
