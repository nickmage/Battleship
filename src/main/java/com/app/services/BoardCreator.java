package com.app.services;

import com.app.entities.BoardCell;
import com.app.entities.Ship;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public final class BoardCreator {

    public ArrayList getBoard(Ship[] ships){
        ArrayList<ArrayList> board = new ArrayList<>();
        char horizontal = 'h';
        char none = '-';
        for (Ship ship: ships) {
            ArrayList<BoardCell> shipCells = new ArrayList<>();
            for (int i = 0; i < ship.getDeckType(); i++) {
                if (ship.getOrientation() == horizontal || ship.getDeckType() == none){
                    shipCells.add(new BoardCell(ship.getX(), ship.getY() + i, ship.getDeckType()));
                } else {
                    shipCells.add(new BoardCell(ship.getX() + i, ship.getY(), ship.getDeckType()));
                }
            }
            board.add(shipCells);
        }
        return board;
    }





}
