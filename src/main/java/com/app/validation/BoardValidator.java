package com.app.validation;

import com.app.entity.Ship;

public class BoardValidator {

    private Ship[] ships;
    private int[][] board = new int[10][10];

    public BoardValidator(Ship[] ships) {
        this.ships = ships;
    }

    public boolean isValidBoard() {
        print();

        return isCorrectShipsQuantity() ;
    }

    private boolean isCorrectShipsQuantity() {
        int oneDeckShips = 0, twoDeckShips = 0, threeDeckShips = 0, fourDeckShips = 0;
        for (Ship ship : ships) {
            int deckType = ship.getDeckType();
            if (deckType == 1) {
                oneDeckShips++;
            } else if (deckType == 2) {
                twoDeckShips++;
            } else if (deckType == 3) {
                threeDeckShips++;
            } else if (deckType == 4) {
                fourDeckShips++;
            }
        }
        return ships.length == 10 && oneDeckShips == 4 && twoDeckShips == 3 && threeDeckShips == 2 && fourDeckShips == 1;
    }

    private boolean isCorrectShipsPositioning(){



       return true;
    }

















    private void print() {
        for (Ship ship : ships) {
            for (int i = 0; i < ship.getDeckType(); i++) {
                if (ship.getPosition() == 'h') {
                    board[ship.getX()][ship.getY() + i] = ship.getDeckType();
                } else if (ship.getPosition() == 'v') {
                    board[ship.getX() + i][ship.getY()] = ship.getDeckType();
                } else if (ship.getPosition() == '-') {
                    board[ship.getX()][ship.getY()] = ship.getDeckType();
                }
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }


}
