package com.sport.infoquest.entity;

import java.util.ArrayList;

/**
 * Created by Ionut Neagu on 05/06/2017.
 */

public class CurrentScore {
    private String track;
    private int correctAnswer;
    private int wrongAnswer;
    private int currentScore;
    private int totalQuests;
    private int remainingQuests;
    private ArrayList<Quests> all;
    private ArrayList<Quests> inProgress;

    public CurrentScore(){

    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(int wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getTotalQuests() {
        return totalQuests;
    }

    public void setTotalQuests(int totalQuests) {
        this.totalQuests = totalQuests;
    }

    public ArrayList<Quests> getAll() {
        return all;
    }

    public void setAll(ArrayList<Quests> all) {
        this.all = all;
    }

    public ArrayList<Quests> getInProgress() {
        return inProgress;
    }

    public void setInProgress(ArrayList<Quests> inProgress) {
        this.inProgress = inProgress;
    }

    public int getRemainingQuests() {
        return remainingQuests;
    }

    public void setRemainingQuests(int remainingQuests) {
        this.remainingQuests = remainingQuests;
    }
}
