package com.app.services;

import com.app.entities.BoardCell;
import com.app.entities.Ship;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
public class BoardCreator {

    public ArrayList<ArrayList<BoardCell>> getShips(Ship[] ships){
        ArrayList<ArrayList<BoardCell>> board = new ArrayList<>();
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

    public HashSet<BoardCell> getBoard(Ship[] ships){
        HashSet<BoardCell> board = new HashSet<>();
        char horizontal = 'h';
        char none = '-';
        for (Ship ship: ships) {
            for (int i = 0; i < ship.getDeckType(); i++) {
                if (ship.getOrientation() == horizontal || ship.getDeckType() == none){
                    board.add(new BoardCell(ship.getX(), ship.getY() + i, ship.getDeckType()));
                } else {
                    board.add(new BoardCell(ship.getX() + i, ship.getY(), ship.getDeckType()));
                }
            }
        }
        return board;
    }

}