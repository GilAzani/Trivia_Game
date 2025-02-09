package com.gilazani.triviagame;

public class LeaderboardItem {
    private String playerName;
    private String score;

    public LeaderboardItem(String playerName, String score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}