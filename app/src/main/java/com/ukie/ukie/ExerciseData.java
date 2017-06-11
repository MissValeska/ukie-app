package com.ukie.ukie;

/**
 * Created by Valeska on 10/06/2017.
 */

public class ExerciseData {

    private String url;
    private String text;
    private int exercise;
    private int module;
    private int QuestionBlock;

    public ExerciseData(String url, String text, int exercise, int module, int QuestionBlock) {
        this.url = url;
        this.text = text;
        this.exercise = exercise;
        this.module = module;
        this.QuestionBlock = QuestionBlock;
    }

    public int getQuestionBlock() {
        if(QuestionBlock != -1) {
            return QuestionBlock;
        }
        else {
            return -1;
        }
    }

    public void setQuestionBlock(int qu) {
        if(qu != -1) {
            QuestionBlock = qu;
        }
    }

    public int getModule() {
        if(module != -1) {
            return module;
        }
        else {
            return -1;
        }
    }

    public void setModule(int mod) {
        if(mod != -1) {
            module = mod;
        }
    }

    public int getExercise() {
        if(exercise != -1) {
            return exercise;
        }
        else {
            return -1;
        }
    }

    public void setExercise(int exc) {
        if(exc != -1) {
            exercise = exc;
        }
    }

    public void setUrl(String str) {
        if(str != null) {
            url = str;
        }
    }

    public void setText(String str) {
        if(str != null) {
            text = str;
        }
    }

    public String getUrl() {
        if(url != null) {
            return url;
        }
        else {
            return null;
        }
    }
    public String getText() {
        if(text != null) {
            return text;
        }
        else {
            return null;
        }
    }
}