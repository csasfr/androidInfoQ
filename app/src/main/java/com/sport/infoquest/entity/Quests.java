package com.sport.infoquest.entity;

import java.io.Serializable;

/**
 * Created by Ionut Neagu on 04/06/2017.
 */

public class Quests implements Serializable {
    private String id;
    private String value;

    public Quests(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
