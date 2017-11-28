package com.sport.infoquest.util;

import com.google.firebase.auth.FirebaseUser;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.ListOfGames;
import com.sport.infoquest.entity.Question;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.enums.UserNaming;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

import static com.sport.infoquest.enums.UserNaming.CREDIT;

/**
 * Created by Ionut on 18/10/2016.
 */
public class Factory {

    public static void createUser(JSONResponse response) {
        final JSONObject object = response.getJsonObject();
        User user = User.getInstance();

        try {
            if (object != null) {
                if (object.getString("email") != null) {
                    user.setUsername(object.getString("email"));
                } else if (object.getString("phoneNumber") != null)
                    user.setUsername(object.getString("phoneNumber"));

                user.setCredit(object.getInt("credit"));
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

//    public static void createGame(JSONResponse response) {
//        final JSONArray gamesArray = response.getJsonArray();
//        ListOfGames listOfGames = ListOfGames.getInstance();
//        try {
//            for (int i = 0; i < gamesArray.length(); i++) {
//                JSONObject object = gamesArray.getJSONObject(i);
//                Game game = new Game();
//                if (object != null) {
//                    if (object.getString("id") != null) {
//                        game.setId(object.getString("id"));
//                    }
//                    if (object.getString("name") != null) {
//                        game.setName(object.getString("name"));
//                    }
//                    if (object.getString("description") != null) {
//                        game.setDescription(object.getString("description"));
//                    }
//                    if (object.getString("hint") != null) {
//                        game.setHint(object.getString("hint"));
//                    }
//                    if (object.getString("cost") != null) {
//                        game.setCost(object.getString("cost"));
//                    }
//                    if (object.getString("time") != null) {
//                        game.setTime(object.getString("time"));
//                    }
//
//                    if (object.getString("hintAndId") != null) {
//                        game.setHintAndId(object.getString("hintAndId"));
//                    }
//                    game.createHashMapOfHintsAndID();
//                    listOfGames.getFinishedGames().put(game.getId(), game);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public static User createUser (final FirebaseUser fbUser){
        User newUser = new User();
        newUser.setUid(fbUser.getUid());
        newUser.setEmail(fbUser.getEmail());
        if (fbUser.getPhotoUrl() != null) {
            newUser.setPhotoUrl(fbUser.getPhotoUrl().toString());
        }
        newUser.setCredit(0);
        if  (fbUser.getDisplayName() == null){
            newUser.setUsername(fbUser.getEmail().contains("@")  ? fbUser.getEmail().split("@")[0] : fbUser.getEmail());
        } else {
            newUser.setUsername(fbUser.getDisplayName());
        }
        return newUser;
    }

    public static User createUserFromMap(final Map<String, String> userMap) {
        User user = new User();
        Set<Map.Entry<String, String>> entrySet = userMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet){
           switch (entry.getKey()){
               case "credit":
                   user.setCredit(Integer.valueOf(entry.getValue()));
                   break;
               case "currentScore":
                   user.setCurrentScore(entry.getValue());
                   break;
               case "email":
                   user.setEmail(entry.getValue());
                   break;
               case "onTrack":
                   user.setOnTrack(Boolean.valueOf(entry.getValue()));
           }
        }
//        if (userMap.containsKey("credit")){
//            user.setCredit(userMap.);
//        }
        return null;
    }
}
