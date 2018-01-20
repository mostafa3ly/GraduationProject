package com.example.mostafa.graduationproject.models;

/**
 * Created by ahmed on 12/18/17.
 */

public class Answer {

    private String _id;
    private String answer;
    private int count;

    public Answer(String _id, String answer, int count) {
        this._id = _id;
        this.answer = answer;
        this.count = count;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
