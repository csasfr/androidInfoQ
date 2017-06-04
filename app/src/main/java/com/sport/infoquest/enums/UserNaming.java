package com.sport.infoquest.enums;

/**
 * Created by Ionut Neagu on 04/06/2017.
 */

public enum UserNaming {
    CREDIT("credit");

    private String name;

    UserNaming(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
