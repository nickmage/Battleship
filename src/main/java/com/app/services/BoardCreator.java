package com.app.services;

import com.app.entities.BoardCell;
import com.app.entities.Ship;

import java.util.ArrayList;

public final class BoardCreator {
    private Ship [] ships;
    private ArrayList<BoardCell> board = new ArrayList<>();

    public ArrayList getBoard(Ship[] ships){
        this.ships = ships;
        fillBoard();
        return board;
    }

    private void fillBoard(){
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
    }



}
