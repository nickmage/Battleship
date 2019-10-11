package com.app.services;

import com.app.models.BoardCell;
import com.app.models.Ship;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BoardCreator {

    public ArrayList<ArrayList<BoardCell>> getShips(Ship[] ships){
        ArrayList<ArrayList<BoardCell>> board = new ArrayList<>();
        for (Ship ship: ships) {
            ArrayList<BoardCell> shipCells = new ArrayList<>();
            addCells(ship, shipCells);
            board.add(shipCells);
        }
        return board;
    }

    public ArrayList<BoardCell> getBoard(Ship[] ships){
        ArrayList<BoardCell> board = new  ArrayList<>();
        for (Ship ship: ships) {
            addCells(ship, board);
        }
        return board;
    }

    private void addCells(Ship ship,  ArrayList<BoardCell> list){
        for (int i = 0; i < ship.getDeckType(); i++) {
            char horizontal = 'h';
            char none = '-';
            if (ship.getOrientation() == horizontal || ship.getDeckType() == none){
                list.add(new BoardCell(ship.getX(), ship.getY() + i, ship.getDeckType()));
            } else {
                list.add(new BoardCell(ship.getX() + i, ship.getY(), ship.getDeckType()));
            }
        }
    }

    /** turnmaker used by restoration
     *
     * abstract factory
     * builder
     * factory method
     * lazy initialisation
     * singleton(ссылка на себя, с памяти не выкидывается)
     * fasade
     * decorator
     * composite
     * chain of responsibility
     * command(action)
     * iterator/cursor
     * mediator
     * memento
     * observer
     * state
     * visitor
     */
}