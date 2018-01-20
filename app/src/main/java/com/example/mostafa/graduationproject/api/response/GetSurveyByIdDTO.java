package com.example.mostafa.graduationproject.api.response;


import com.example.mostafa.graduationproject.models.Question;

import java.util.List;

/**
 * Created by ahmed on 12/18/17.
 */



public class GetSurveyByIdDTO {

    public String title;
    public List<Question> questions = null;
    public boolean restricted;
    public String user_id;
    public String _id;

    public GetSurveyByIdDTO(String title, List<Question> questions, boolean restricted, String user_id, String _id) {
        this.title = title;
        this.questions = questions;
        this.restricted = restricted;
        this.user_id = user_id;
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
