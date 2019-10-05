package com.app.services;

import com.app.cache.Room;
import com.app.entities.BoardCell;
import com.app.entities.RemainingShips;
import com.app.response_wrappers.GameInitResponseWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameInitResponseCreator {

    public GameInitResponseWrapper getResponse(Room room, String playerId) {
        GameInitResponseWrapper response = new GameInitResponseWrapper();
        if (playerId.equals(room.getPlayer1Id().toString())) {
            response.setPlayerBoard(room.getPlayer1Board());
            response.setPlayerShips(getRemainingShips(room.getPlayer1Ships()));
            response.setEnemyShips(getRemainingShips(room.getPlayer2Ships()));
            /*response.setPlayerShips(room.getRemainingShipsOfPlayer1());
            response.setEnemyShips(room.getRemainingShipsOfPlayer2());*/
            if (room.getEnemyBoardForPlayer1() != null) {
                response.setEnemyBoard(room.getEnemyBoardForPlayer1());
            }
            response.setEnemyName(room.getPlayer2Name());
            response.setMyTurn(room.getCurrentPlayer() == 1);
        } else {
            response.setPlayerBoard(room.getPlayer2Board());
            response.setPlayerShips(getRemainingShips(room.getPlayer2Ships()));
            response.setEnemyShips(getRemainingShips(room.getPlayer1Ships()));
            /*response.setPlayerShips(room.getRemainingShipsOfPlayer2());
            response.setEnemyShips(room.getRemainingShipsOfPlayer1());*/
            if (room.getEnemyBoardForPlayer2() != null) {
                response.setEnemyBoard(room.getEnemyBoardForPlayer2());
            }
            response.setEnemyName(room.getPlayer1Name());
            response.setMyTurn(room.getCurrentPlayer() == 2);
        }
        response.setWinner(room.getWinner());
        return response;
    }

    private RemainingShips getRemainingShips(ArrayList<ArrayList<BoardCell>> ships) {
        RemainingShips remainingShips = new RemainingShips();
        int oneDeck = 0;
        int twoDeck = 0;
        int threeDeck = 0;
        int fourDeck = 0;
        for (ArrayList<BoardCell> ship : ships) {
            int status = isShipInOrder(ship);
            if (status == 1) {
                oneDeck++;
            } else if (status == 2) {
                twoDeck++;
            } else if (status == 3) {
                threeDeck++;
            } else if (status == 4) {
                fourDeck++;
            }
        }
        remainingShips.setOneDeckShips(oneDeck);
        remainingShips.setTwoDeckShips(twoDeck);
        remainingShips.setThreeDeckShips(threeDeck);
        remainingShips.setFourDeckShips(fourDeck);
        return remainingShips;
    }

    //return 0 if ship is sunken or ship's deck type
    private int isShipInOrder(ArrayList<BoardCell> ship) {
        int deckType = ship.get(0).getValue();
        int count = 0;
        for (BoardCell cell : ship) {
            if (cell.getValue() > 0) {
                count++;
            }
        }
        return count == 0 ? 0 : deckType;
    }
}
