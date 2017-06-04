package com.sport.infoquest.entity;

import java.io.Serializable;

/**
 * Created by Ionut on 18/10/2016.
 */
public class Question implements Serializable {
    private String id;
    private String question;
    private String correctAnswer;
    private String otherAnswer;
    private String point;

    public String getText() {
        return question;
    }

    public void setText(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getOtherAnswer() {
        return otherAnswer;
    }

    public void setOtherAnswer(String otherAnswer) {
        this.otherAnswer = otherAnswer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

}
