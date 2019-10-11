package com.app.services;

import com.app.cache.Room;
import com.app.cache.RoomCache;
import com.app.exception.WinnerException;
import com.app.response_wrappers.GameStatusResponseWrapper;
import org.springframework.stereotype.Service;

@Service
public class GameStatusResponseCreator {
    private final RemainingShipsCreator remainingShips;

    public GameStatusResponseCreator(RemainingShipsCreator remainingShips) {
        this.remainingShips = remainingShips;
    }

    public GameStatusResponseWrapper getResponse(Room room, String playerId) {
        GameStatusResponseWrapper response = new GameStatusResponseWrapper();
        if (playerId.equals(room.getPlayer1Id().toString())) {
            setResponseForPlayer1(response, room);
        } else if (playerId.equals(room.getPlayer2Id().toString())) {
            setResponseForPlayer2(response, room);
        }
        return response;
    }

    private void setResponseForPlayer1(GameStatusResponseWrapper response, Room room) {
        response.setPlayerBoard(room.getPlayer1Board());
        response.setMyTurn(room.getCurrentPlayer() == 1);
        try {
            response.setPlayerShips(remainingShips.getRemainingShips(room.getPlayer1Ships()));
        } catch (WinnerException e) {
            actionOnWinnerPresence(response, room, 1);
            /*response.setPlayerShips(remainingShips.getDestroyedShips());
            response.setWinner(room.getWinner() == 1 ? CURRENT_PLAYER : OPPONENT);
            RoomCache.rooms.remove(room.getRoomId().toString());*/
        }
    }

    private void setResponseForPlayer2(GameStatusResponseWrapper response, Room room) {
        response.setPlayerBoard(room.getPlayer2Board());
        try {
            response.setPlayerShips(remainingShips.getRemainingShips(room.getPlayer2Ships()));
        } catch (WinnerException e) {
            actionOnWinnerPresence(response, room, 2);
            /*response.setPlayerShips(remainingShips.getDestroyedShips());
            response.setWinner(room.getWinner() == 2 ? CURRENT_PLAYER : OPPONENT);
            RoomCache.rooms.remove(room.getRoomId().toString());*/
        }
        response.setMyTurn(room.getCurrentPlayer() == 2);
    }

    private void actionOnWinnerPresence(GameStatusResponseWrapper response, Room room, int playerNumber){
        response.setPlayerShips(remainingShips.getDestroyedShips());
        int currentPlayer = 1;
        int opponent = -1;
        response.setWinner(room.getWinner() == playerNumber ? currentPlayer : opponent);
        RoomCache.rooms.remove(room.getRoomId().toString());
    }

}
