package com.app.response_wrappers;

public class ScoreboardWrapper {

    private String username;
    private int wins;
    private int loses;

    public ScoreboardWrapper(String username, int wins, int loses) {
        this.username = username;
        this.wins = wins;
        this.loses = loses;
    }

    public String getUsername() {
        return username;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

}
