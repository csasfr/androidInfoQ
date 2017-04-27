package com.sport.infoquest.enums;

public enum Drawer {

    HOME("home_fragment", 0),
    SELECT_GAME("select_game_fragment", 1),
    CURRENT_GAME("current_game_fragment", 2),
    GENERAL_SCORE("general_score", 3),
    CURRENT_SCORE("current_score", 4),
    BUY_COINS("buy_coins", 5),
    LOGOUT("Logout", 6),
    START_GAME("start_game_fragment", 8),
    SCAN_QR("scan_qr_fragment", 9);

    private String name;
    private int position;

    Drawer(String name, int i) {
        this.name = name;
        this.position = i;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}
