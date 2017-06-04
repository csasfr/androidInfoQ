package com.sport.infoquest.entity;

import java.util.ArrayList;

/**
 * Created by Ionut on 14/12/2016.
 */
public class Game{
    private int cost;
    String description;
    String name;
    private ArrayList<Quests> quests;
    int time;
    int totalQuestions;

    public Game(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getHintAndId() {
        return "";
    }


    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


    public ArrayList<Quests> getQuests() {
        return quests;
    }

    public void setQuests(ArrayList<Quests> quests) {
        this.quests = quests;
    }
}
