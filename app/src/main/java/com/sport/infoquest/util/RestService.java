package com.sport.infoquest.util;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ionut on 17/10/2016.
 */
public class RestService {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String HOST_URL = "http://infoquest-restgps.rhcloud.com/api/ws";
    private static final String GET_ALL_GAMES = "/all";
    private static final String GET_QUESTION_BY_ID = "/question/";
    private static final String GET_VALIDATION = "/user/checkUser/";
    private static final String GET_USER = "/user/getUser/";
    private static final String GET_QUESTIONS = "/user/checkQuestion/";
    private static final String SET_TRACK = "/user/setChangeOnTrack/";
    private static final String SET_CORRECT_ANSWER = "/user/setCorrectAnswer/";
    private static final String SET_WRONG_ANSWER = "/user/setWrongAnswer/";

    private static final String POST_QUESTION_BY_ID = "/user/checkQuestionId/";
    private static final String POST_START_GAME_WITH_CHECK_CREDIT = "/user/setCurrentGame/";
    private static final String POST_IS_CURRENT_GAME = "/user/checkCurrentGame/";
    private static final String POST_EXIT_GAME = "/user/exitGame/";
    private static final String POST_CHECK_QUESTION_AND_SET = "/user/checkAndSet/";
    private static final String TAG = "RestService";

    public static JSONResponse getAllGames() throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + GET_ALL_GAMES);
            con = (HttpURLConnection) obj.openConnection();

            setGETRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                response.setResponseCode(responseCode);
                response.setJsonArray(new JSONArray(jsonString));
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    public static JSONResponse postQuestionById(String phoneOrEmail, String questionId) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + POST_QUESTION_BY_ID + phoneOrEmail + "/" + questionId);
            con = (HttpURLConnection) obj.openConnection();

            setPOSTRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                if (jsonString.contains(StatusCode.OK.getDescription())) {
                    response.setStatusCode(StatusCode.OK);
                    response.setResponseCode(StatusCode.OK.getCode());
                } else {
                    response.setStatusCode(StatusCode.NOT_FOUND);
                    response.setResponseCode(StatusCode.NOT_FOUND.getCode());
                }
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    public static JSONResponse getQuestionById(String id) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + GET_QUESTION_BY_ID + id);
            con = (HttpURLConnection) obj.openConnection();

            setGETRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                response.setResponseCode(responseCode);
                response.setStatusCode(StatusCode.OK);
                response.setJsonObject(new JSONObject(jsonString));
            } else if (responseCode == StatusCode.NOT_FOUND.getCode()) {
                response.setStatusCode(StatusCode.NOT_FOUND);
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    public static JSONResponse getValidation(String userOfPhone, String password) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + GET_VALIDATION + userOfPhone + "/" + password);
            con = (HttpURLConnection) obj.openConnection();

            setGETRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                String result[] = jsonString.split("\\\"");
                response.setStatusCode(StatusCode.getStatusFromDescriprion(result[1]));
                response.setResponseCode(responseCode);
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    public static JSONResponse getUserInformation(String userOfPhone) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + GET_USER + userOfPhone);
            con = (HttpURLConnection) obj.openConnection();

            setGETRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                response.setResponseCode(responseCode);
                response.setJsonObject(new JSONObject(jsonString));
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    public static String getUserQuestions(final String userOfPhone) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString = "";

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + GET_QUESTIONS + userOfPhone);
            con = (HttpURLConnection) obj.openConnection();

            setGETRequestParameters(con);

            response = new JSONResponse();
            Log.w(RestService.class.getName(), "response " + con.getResponseCode());
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                return jsonString;
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return jsonString;
    }

    public static JSONResponse setTrack(String userOfPhone, String onTrack, String trackName, String costs) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + SET_TRACK + userOfPhone + "/" + onTrack + "/" + trackName + "/" + costs);
            con = (HttpURLConnection) obj.openConnection();

            setPOSTRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                String result[] = jsonString.split("\\\"");
                if (result.length > 1)
                    response.setStatusCode(StatusCode.getStatusFromDescriprion(result[1]));
                else
                    response.setStatusCode(StatusCode.getStatusFromDescriprion(jsonString.replace("\n", "").replace("\r", "")));
                response.setResponseCode(responseCode);
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    public static JSONResponse setCorrectAnswer(String userOfPhone, String scannedQuestion) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + SET_CORRECT_ANSWER + userOfPhone + "/" + scannedQuestion);
            con = (HttpURLConnection) obj.openConnection();

            setPOSTRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                if (jsonString.contains(StatusCode.OK.getDescription())) {
                    response.setStatusCode((StatusCode.OK));
                    response.setResponseCode(StatusCode.OK.getCode());
                } else if (jsonString.contains(StatusCode.GONE.getDescription())) {
                    response.setStatusCode((StatusCode.GONE));
                    response.setResponseCode(StatusCode.GONE.getCode());
                } else {
                    response.setStatusCode((StatusCode.NOT_FOUND));
                    response.setResponseCode(StatusCode.NOT_FOUND.getCode());
                }
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    public static JSONResponse setWrongAnswer(String userOfPhone, String scannedQuestion) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + SET_WRONG_ANSWER + userOfPhone + "/" + scannedQuestion);
            con = (HttpURLConnection) obj.openConnection();

            setPOSTRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                if (jsonString.contains(StatusCode.OK.getDescription())) {
                    response.setStatusCode((StatusCode.OK));
                    response.setResponseCode(StatusCode.OK.getCode());
                } else if (jsonString.contains(StatusCode.GONE.getDescription())) {
                    response.setStatusCode((StatusCode.GONE));
                    response.setResponseCode(StatusCode.GONE.getCode());
                } else {
                    response.setStatusCode((StatusCode.NOT_FOUND));
                    response.setResponseCode(StatusCode.NOT_FOUND.getCode());
                }
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    public static JSONResponse postStartGameWithCreditCheck(String userOfPhone, String gameName) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + POST_START_GAME_WITH_CHECK_CREDIT + userOfPhone + "/" + gameName);
            Log.i(TAG, obj.getQuery() + " " + obj.getPath());
            con = (HttpURLConnection) obj.openConnection();

            setPOSTRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                if (jsonString.contains(StatusCode.OK.getDescription())) {
                    response.setResponseCode(StatusCode.OK.getCode());
                } else if (jsonString.contains(StatusCode.CONFLICT.getDescription())) {
                    response.setResponseCode(StatusCode.CONFLICT.getCode());
                } else {
                    response.setResponseCode(StatusCode.NOT_FOUND.getCode());
                }
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    public static JSONResponse postIsCurrentGame(String userOfPhone, String gameName) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + POST_IS_CURRENT_GAME + userOfPhone + "/" + gameName);
            con = (HttpURLConnection) obj.openConnection();

            setPOSTRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                if (jsonString.contains(StatusCode.OK.getDescription())) {
                    response.setResponseCode(StatusCode.OK.getCode());
                } else {
                    response.setResponseCode(StatusCode.NOT_FOUND.getCode());
                }
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    public static JSONResponse postExitGame(String userOfPhone) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + POST_EXIT_GAME + userOfPhone);
            con = (HttpURLConnection) obj.openConnection();

            setPOSTRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                if (jsonString.contains(StatusCode.OK.getDescription())) {
                    response.setResponseCode(StatusCode.OK.getCode());
                } else {
                    response.setResponseCode(StatusCode.NOT_FOUND.getCode());
                }
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }

    // this is used to check if the user already scanned a question, if it doesn't, update user question list
    public static JSONResponse postCheckAndSet(String userOfPhone, String questionId) throws IOException, JSONException {
        int responseCode = 0;
        JSONResponse response = null;
        String jsonString;

        HttpURLConnection con = null;
        try {
            URL obj = new URL(HOST_URL + POST_CHECK_QUESTION_AND_SET + userOfPhone + "/" + questionId);
            con = (HttpURLConnection) obj.openConnection();

            setPOSTRequestParameters(con);

            response = new JSONResponse();
            responseCode = con.getResponseCode();
            if (responseCode == StatusCode.OK.getCode()) {
                jsonString = readResponse(con);
                if (jsonString.contains(StatusCode.OK.getDescription())) {
                    response.setResponseCode(StatusCode.OK.getCode());
                } else if (jsonString.contains(StatusCode.ALREADY_EXISTS.getDescription())) {
                    response.setResponseCode(StatusCode.ALREADY_EXISTS.getCode());
                } else {
                    response.setResponseCode(StatusCode.NOT_FOUND.getCode());
                }
            }
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "No internet connection...", e);
            }
        }
        return response;
    }


    private static void setGETRequestParameters(HttpURLConnection con) throws ProtocolException {
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json");
    }

    private static void setGETRequestTextParameters(HttpURLConnection con) throws ProtocolException {
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "text/plain");
    }

    private static void setPOSTRequestParameters(HttpURLConnection con) throws ProtocolException {
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json");
    }

    @NonNull
    private static String readResponse(HttpURLConnection con) throws IOException {
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line + '\n');
        }
        return stringBuilder.toString();
    }
}
