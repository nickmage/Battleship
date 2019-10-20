package com.app.validation;

import com.app.models.Ship;

public class BoardValidator {

    private Ship[] ships;
    private final char HORIZONTAL = 'h';
    private final char VERTICAL = 'v';
    private final char NONE = '-';

    public BoardValidator(Ship[] ships) {
        this.ships = ships;
    }

    public boolean isValidBoard() {
        return notNull() && isCorrectShipsQuantity() && isCorrectShipsPlacing()
                && isCorrectShipOrientation() && isCorrectShipsRelativePositioning();
    }

    private boolean notNull() {
        if (ships == null) {
            return false;
        } else {
            for (Ship ship : ships) {
                if (ship == null) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean isCorrectShipsQuantity() {
        int oneDeckShips = 0, twoDeckShips = 0, threeDeckShips = 0, fourDeckShips = 0;
        int oneDeckShip = 1, twoDeckShip = 2, threeDeckShip = 3, fourDeckShip = 4;
        for (Ship ship : ships) {
            int deckType = ship.getDeckType();
            if (deckType == oneDeckShip) {
                oneDeckShips++;
            } else if (deckType == twoDeckShip) {
                twoDeckShips++;
            } else if (deckType == threeDeckShip) {
                threeDeckShips++;
            } else if (deckType == fourDeckShip) {
                fourDeckShips++;
            }
        }
        return ships.length == 10 && oneDeckShips == 4 && twoDeckShips == 3 && threeDeckShips == 2 && fourDeckShips == 1;
    }

    private boolean isCorrectShipsPlacing() {
        for (Ship ship : ships) {
            if (ship.getOrientation() == HORIZONTAL || ship.getOrientation() == NONE) {
                if (isShipOutOfBound(ship.getX(), ship.getY() + ship.getDeckType() - 1)) {
                    return false;
                }
            } else if (ship.getOrientation() == VERTICAL) {
                if (isShipOutOfBound(ship.getX() + ship.getDeckType() - 1, ship.getY())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isShipOutOfBound(int x, int y) {
        int boardSize = 10;
        return x < 0 || x >= boardSize || y < 0 || y >= boardSize;
    }

    private boolean isCorrectShipOrientation() {
        for (Ship ship : ships) {
            char orientation = ship.getOrientation();
            if (!(orientation == HORIZONTAL || orientation == VERTICAL || orientation == NONE)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCorrectShipsRelativePositioning() {
        for (int i = 0; i < ships.length; i++) {
            for (int j = i + 1; j < ships.length; j++) {
                if (!checkShipPositioning(ships[i], ships[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkShipPositioning(Ship firstShip, Ship secondShip) {
        int x = firstShip.getX();
        int y = firstShip.getY();
        if (firstShip.getOrientation() == HORIZONTAL || firstShip.getOrientation() == NONE) {
            for (int i = 0; i < firstShip.getDeckType(); i++) {
                y += (i == 0) ? 0 : 1;
                if (hasCollision(x, y, secondShip)) {
                    return false;
                }
            }
        } else if (firstShip.getOrientation() == VERTICAL) {
            for (int i = 0; i < firstShip.getDeckType(); i++) {
                x += (i == 0) ? 0 : 1;
                if (hasCollision(x, y, secondShip)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasCollision(int x, int y, Ship secondShip) {
        int secondX = secondShip.getX();
        int secondY = secondShip.getY();
        if (secondShip.getOrientation() == HORIZONTAL || secondShip.getOrientation() == NONE) {
            for (int j = 0; j < secondShip.getDeckType(); j++) {
                if (Math.abs(x - secondX) <= 1 && Math.abs(y - secondY - j) <= 1) {
                    return true;
                }
            }
        } else {
            for (int j = 0; j < secondShip.getDeckType(); j++) {
                if (Math.abs(x - secondX - j) <= 1 && Math.abs(y - secondY) <= 1) {
                    return true;
                }
            }
        }
        return false;
    }

}
