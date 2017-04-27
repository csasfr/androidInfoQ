package com.sport.infoquest.util;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Ionut on 17/10/2016.
 */
public class JSONResponse {

    private int responseCode;
    private StatusCode statusCode;
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    public JSONResponse() {
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
