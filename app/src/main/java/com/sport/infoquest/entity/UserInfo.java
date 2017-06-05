package com.sport.infoquest.entity;

/**
 * Created by Ionut Neagu on 05/06/2017.
 */

public class UserInfo {
    private int credit;
    private String email;
    private String username;
    private boolean onTrack;
    private String uid;

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOnTrack() {
        return onTrack;
    }

    public void setOnTrack(boolean onTrack) {
        this.onTrack = onTrack;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
