package com.sport.infoquest.entity;

import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ionut on 21/10/2016.
 */
public class User {
    private static User instance = null;
    private String username;
    private String credit;
    private String currentTrack;
    private String currentScore;
    private String correctNumber;
    private String wrongNumer;
    private String remainingQuestions;
    private String scannedQuestions;
    private String totalQR;
    private String iconId;
    private boolean isOnTrack;
    private Game currentGame;
    private Game selectedGame;
    private HashMap<String, String> codeTextHint;
    private List<String> marked = new ArrayList<>();

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

    public boolean hasCredit(Game game) {
        if (Integer.valueOf(credit) - Integer.valueOf(game.getCost()) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean payCredit(Game game) {
        JSONResponse response;

        if (Integer.valueOf(credit) - Integer.valueOf(game.getCost()) > 0) {
            credit = String.valueOf(Integer.valueOf(credit) - Integer.valueOf(game.getCost()));
            try {
                response = RestService.setTrack(this.getUsername(), String.valueOf(this.isOnTrack()), game.getName(), game.getCost());

                if (Utils.checkStatusResponse(response)) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return false;
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
}
