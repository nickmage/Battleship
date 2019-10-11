package com.app.services;

import com.app.models.BoardCell;
import com.app.models.RemainingShips;
import com.app.exception.WinnerException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
class RemainingShipsCreator {

    RemainingShips getRemainingShips(ArrayList<ArrayList<BoardCell>> ships) throws WinnerException {
        int oneDeck = 0, twoDeck = 0, threeDeck = 0, fourDeck = 0;
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
        if (oneDeck == 0 && twoDeck == 0 && threeDeck == 0 && fourDeck == 0) {
            throw new WinnerException();
        }
        return new RemainingShips(oneDeck, twoDeck, threeDeck, fourDeck);
    }

    //return 0 if ship is sunken or ship's deck type
    private int isShipInOrder(ArrayList<BoardCell> ship) {
        int deckType = Math.abs(ship.get(0).getValue());
        int count = 0;
        for (BoardCell cell : ship) {
            if (cell.getValue() > 0) {
                count++;
            }
        }
        return count == 0 ? 0 : deckType;
    }

    RemainingShips getDestroyedShips(){
        return new RemainingShips(0, 0, 0, 0);
    }

}
