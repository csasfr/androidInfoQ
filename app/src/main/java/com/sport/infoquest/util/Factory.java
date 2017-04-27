package com.sport.infoquest.util;

import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.ListOfGames;
import com.sport.infoquest.entity.Question;
import com.sport.infoquest.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ionut on 18/10/2016.
 */
public class Factory {

    public static Question createQuestionObject(JSONResponse response) {
        final JSONObject object = response.getJsonObject();
        Question question = new Question();

        try {
            question.setId(object.getInt("id"));
            question.setText(object.getString("question"));
            question.setCorrectAnswer(object.getString("correctAnswer"));
            question.setWrongAnswer(object.getString("otherAnswer"));
            question.setPoint(object.getString("point"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return question;
    }

    public static void createUser(JSONResponse response) {
        final JSONObject object = response.getJsonObject();
        User user = User.getInstance();

        try {
            if (object != null) {
                if (object.getString("email") != null) {
                    user.setUsername(object.getString("email"));
                } else if (object.getString("phoneNumber") != null)
                    user.setUsername(object.getString("phoneNumber"));

                user.setCredit(object.getString("credit"));
                user.setCurrentTrack(object.getString("currentTrack"));
                user.setScannedQuestions(object.getString("scannedQuestions"));
                user.setCurrentScore(object.getString("currentScore"));
                //user.set(object.getString("credit"));
                if (!object.getString("isOnTrack").equals("") && object.getString("isOnTrack") != null) {
                    user.setOnTrack(object.getBoolean("isOnTrack"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void createGame(JSONResponse response) {
        final JSONArray gamesArray = response.getJsonArray();
        ListOfGames listOfGames = ListOfGames.getInstance();
        try {
            for (int i = 0; i < gamesArray.length(); i++) {
                JSONObject object = gamesArray.getJSONObject(i);
                Game game = new Game();
                if (object != null) {
                    if (object.getString("id") != null) {
                        game.setId(object.getString("id"));
                    }
                    if (object.getString("name") != null) {
                        game.setName(object.getString("name"));
                    }
                    if (object.getString("description") != null) {
                        game.setDescription(object.getString("description"));
                    }
                    if (object.getString("hint") != null) {
                        game.setHint(object.getString("hint"));
                    }
                    if (object.getString("cost") != null) {
                        game.setCost(object.getString("cost"));
                    }
                    if (object.getString("time") != null) {
                        game.setTime(object.getString("time"));
                    }

                    if (object.getString("hintAndId") != null) {
                        game.setHintAndId(object.getString("hintAndId"));
                    }
                    game.createHashMapOfHintsAndID();
                    listOfGames.getGames().put(game.getId(), game);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
