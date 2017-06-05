package com.sport.infoquest.entity;

/**
 * Created by Ionut Neagu on 05/06/2017.
 */

public class CurrentUser {
    private String uid;
    private String email;
    private boolean onTrack;
    private int credit;
    private String username;

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

    public boolean isOnTrack() {
        return onTrack;
    }

    public void setOnTrack(boolean onTrack) {
        this.onTrack = onTrack;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
