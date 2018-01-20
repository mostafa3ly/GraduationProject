package com.example.mostafa.graduationproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mosta on 12/12/2017.
 */

public class Question {

    private String question;
    private List<String> answers = new ArrayList<>();
    private int type;

    public Question() {
    }

    public Question(String question, List<String> answers, int type) {
        this.question = question;
        this.answers = answers;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
