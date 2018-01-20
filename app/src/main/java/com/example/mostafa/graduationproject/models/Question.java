package com.example.mostafa.graduationproject.models;

import java.util.ArrayList;

/**
 * Created by ahmed on 12/18/17.
 */

public class Question {

    private String question;
    private String type;
    private String _id;
    private ArrayList<Answer> answers = null;

    public Question(String question, String type, String _id, ArrayList<Answer> answers) {
        this.question = question;
        this.type = type;
        this._id = _id;
        this.answers = answers;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
