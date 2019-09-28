package com.app.services;

import com.app.entities.BoardCell;
import com.app.entities.Ship;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public final class BoardCreator {

    public ArrayList getBoard(Ship[] ships){
        ArrayList<BoardCell> board = new ArrayList<>();
        char horizontal = 'h';
        char none = '-';
        for (Ship ship: ships) {
            for (int i = 0; i < ship.getDeckType(); i++) {
                if (ship.getDeckType() == horizontal || ship.getDeckType() == none){
                    board.add(new BoardCell(ship.getX(), ship.getY() + i, ship.getDeckType()));
                } else {
                    board.add(new BoardCell(ship.getX() + i, ship.getY(), ship.getDeckType()));
                }
            }
        }
        return board;
    }





}
