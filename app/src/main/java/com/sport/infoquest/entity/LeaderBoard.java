package com.sport.infoquest.entity;

import java.util.ArrayList;

/**
 * Created by Ionut Neagu on 05/06/2017.
 */

public class LeaderBoard {
    private int totalScore;
    private String uid;
    private String email;
    private String displayName;
    private ArrayList<CurrentScore> finishedGames;

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ArrayList<CurrentScore> getFinishedGames() {
        if (finishedGames == null){
            finishedGames = new ArrayList<>();
        }
        return finishedGames;
    }

    public void setFinishedGames(ArrayList<CurrentScore> finishedGames) {
        this.finishedGames = finishedGames;
    }
}
