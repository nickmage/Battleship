package com.app.entity;

public class Ship {
    private int x;
    private int y;
    private char orientation;
    private int deckType;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getOrientation() {
        return orientation;
    }

    public void setOrientation(char orientation) {
        this.orientation = orientation;
    }

    public int getDeckType() {
        return deckType;
    }

    public void setDeckType(int deckType) {
        this.deckType = deckType;
    }
}
