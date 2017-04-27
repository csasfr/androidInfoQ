package com.sport.infoquest.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ionut on 14/12/2016.
 */
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String description;
    private String hint;
    private String cost;
    private String time;
    private String hintAndId;
    private HashMap<String, String> idHintMap = new HashMap<>();
    private List<IdHint> idHintList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHintAndId() {
        return hintAndId;
    }

    public void setHintAndId(String hintAndId) {
        this.hintAndId = hintAndId;
    }

    public void createHashMapOfHintsAndID() {
        this.idHintList = new ArrayList<>();
        String[] idHints = this.getHintAndId().split(";");
        for (String idHint : idHints) {
            String[] ls = idHint.split(",");
            this.idHintList.add(new IdHint(ls[0], ls[1]));
            this.getHashMap().put(ls[0], ls[1]);
        }
    }

    public HashMap<String, String> getHashMap() {
        return this.idHintMap;
    }

    public List<IdHint> getIdHintList() {
        return this.idHintList;
    }

}
