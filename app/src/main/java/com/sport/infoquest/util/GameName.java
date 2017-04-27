package com.sport.infoquest.util;

/**
 * Created by Ionut on 08/03/2017.
 */

public enum GameName {
    EDENLAND("Edenland Balotesti");

    private String gameName;

    GameName(String gameName) {
        this.gameName = gameName;
    }

    public String getNameValue() {
        return this.gameName;
    }
}
