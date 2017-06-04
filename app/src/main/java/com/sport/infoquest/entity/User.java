package com.sport.infoquest.entity;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ionut on 21/10/2016.
 */
public class User {
    private static User instance = null;
    private String uid;
    private String username;
    private String email;
    private String credit;
    private String currentTrack;
    private String currentScore;
    private String correctNumber;
    private String wrongNumer;
    private String remainingQuestions;
    private String scannedQuestions;
    private String totalQR;
    private String iconId;
    private Uri photoUrl;
    private boolean isOnTrack;
    private Game currentGame;
    private Game selectedGame;
    private ArrayList<Game> gameList = new ArrayList<>();
    private HashMap<String, String> codeTextHint;
    private List<String> marked = new ArrayList<>();
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private Query queryRef;
    public static final String TAG = "UserInstance";

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(String currentTrack) {
        this.currentTrack = currentTrack;
    }

    public boolean isOnTrack() {
        return isOnTrack;
    }

    public void setOnTrack(boolean onTrack) {
        isOnTrack = onTrack;
    }


    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public Game getSelectedGame() {
        return selectedGame;
    }

    public void setSelectedGame(Game selectedGame) {
        this.selectedGame = selectedGame;
    }

    public HashMap<String, String> getCodeTextHint() {
        return codeTextHint;
    }

    public void setCodeTextHint(HashMap<String, String> codeTextHint) {
        this.codeTextHint = codeTextHint;
    }

    public String getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(String currentScore) {
        this.currentScore = currentScore;
    }

    public String getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(String correctNumber) {
        this.correctNumber = correctNumber;
    }

    public String getWrongNumer() {
        return wrongNumer;
    }

    public void setWrongNumer(String wrongNumer) {
        this.wrongNumer = wrongNumer;
    }

    public String getRemainingQuestions() {
        return remainingQuestions;
    }

    public void setRemainingQuestions(String remainingQuestions) {
        this.remainingQuestions = remainingQuestions;
    }

    public String getScannedQuestions() {
        return scannedQuestions;
    }

    public void setScannedQuestions(String scannedQuestions) {
        this.scannedQuestions = scannedQuestions;
    }

    public String getTotalQR() {
        return totalQR;
    }

    public void setTotalQR(String totalQR) {
        this.totalQR = totalQR;
    }

    public List<String> getMarked() {
        return marked;
    }

    public void setMarked(List<String> marked) {
        this.marked = marked;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getIconId() {
        return iconId;
    }


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

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<Game> getGameList() {
        return gameList;
    }

    public void setGameList(ArrayList<Game> gameList) {
        this.gameList = gameList;
    }


}
