package com.sport.infoquest.entity;

import java.util.HashMap;

/**
 * Created by Ionut on 14/12/2016.
 */
public class ListOfGames {
    private static ListOfGames instance = null;
    private HashMap<String, Game> games = new HashMap<>();

    public static ListOfGames getInstance() {
        if (instance == null) {
            instance = new ListOfGames();
        }
        return instance;
    }

    public HashMap<String, Game> getGames() {
        return games;
    }

    public void setGames(HashMap<String, Game> games) {
        this.games = games;
    }
}
