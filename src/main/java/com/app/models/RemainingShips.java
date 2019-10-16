package com.app.models;

public class RemainingShips {
    private int oneDeckShips;
    private int twoDeckShips;
    private int threeDeckShips;
    private int fourDeckShips;

    public RemainingShips(int oneDeckShips, int twoDeckShips, int threeDeckShips, int fourDeckShips) {
        this.oneDeckShips = oneDeckShips;
        this.twoDeckShips = twoDeckShips;
        this.threeDeckShips = threeDeckShips;
        this.fourDeckShips = fourDeckShips;
    }

    public int getOneDeckShips() {
        return oneDeckShips;
    }

    public void setOneDeckShips(int oneDeckShips) {
        this.oneDeckShips = oneDeckShips;
    }

    public int getTwoDeckShips() {
        return twoDeckShips;
    }

    public void setTwoDeckShips(int twoDeckShips) {
        this.twoDeckShips = twoDeckShips;
    }

    public int getThreeDeckShips() {
        return threeDeckShips;
    }

    public void setThreeDeckShips(int threeDeckShips) {
        this.threeDeckShips = threeDeckShips;
    }

    public int getFourDeckShips() {
        return fourDeckShips;
    }

    public void setFourDeckShips(int fourDeckShips) {
        this.fourDeckShips = fourDeckShips;
    }
}
