package com.ukie.ukie;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Valeska on 07/06/2017.
 */

public class questionData implements Serializable {

    /*int QuestionCount;
    private int index;
    private int progress;*/
    String conj;
    String conjtype;
    String infin;
    String type;
    int correctAnswer = -1;
    String qAudio1;
    String qAudio2;
    String qAudio3;
    String qAudio4;
    String qImg1;
    String qImg2;
    String qImg3;
    String qImg4;
    String questionText;
    String questionText1;
    String questionText2;
    String dropDown1;
    String dropDown2;

    public String getdropDown2() {
        if(dropDown2 != null) {
            return dropDown2;
        }
        else {
            return null;
        }
    }

    public String getdropDown1() {
        if(dropDown1 != null) {
            return dropDown1;
        }
        else {
            return null;
        }
    }

    public String getquestionText2() {
        if(questionText2 != null) {
            return questionText2;
        }
        else {
            return null;
        }
    }

    public String getquestionText1() {
        if(questionText1 != null) {
            return questionText1;
        }
        else {
            return null;
        }
    }

    public int getInt(String s) {
        switch(s) {
            case "correctAnswer":
                return getCorrectAnswer();
        }
        return -1;
    }

    public String getString(String s) {

        switch(s) {
            case "conj":
                return getConj();
            case "conjtype":
                return getConjType();
            case "infin":
                return getInfin();
            case "type":
                return getType();
            case "qAudio1":
                return getqAudio1();
            case "qAudio2":
                return getqAudio2();
            case "qAudio3":
                return getqAudio3();
            case "qAudio4":
                return getqAudio4();
            case "qImg1":
                return getqImg1();
            case "qImg2":
                return getqImg2();
            case "qImg3":
                return getqImg3();
            case "qImg4":
                return getqImg4();
            case "question-text":
                return getQuestionText();
            case "questionText":
                return getQuestionText();
        }
        return null;
    }

    public int getCorrectAnswer() {
        if(correctAnswer != -1) {
            return correctAnswer;
        }
        else {
            return -1;
        }
    }

    public String getConj() {
        if(conj != null) {
            return conj;
        }
        else {
            return null;
        }
    }

    public String getConjType() {
        if(conjtype != null) {
            return conjtype;
        }
        else {
            return null;
        }
    }

    public String getInfin() {
        if(infin != null) {
            return infin;
        }
        else {
            return null;
        }
    }

    public String getType() {
        if(type != null) {
            return type;
        }
        else {
            return null;
        }
    }

    public String getqAudio1() {
        if(qAudio1 != null) {
            return qAudio1;
        }
        else {
            return null;
        }
    }

    public String getqAudio2() {
        if(qAudio2 != null) {
            return qAudio2;
        }
        else {
            return null;
        }
    }

    public String getqAudio3() {
        if(qAudio3 != null) {
            return qAudio3;
        }
        else {
            return null;
        }
    }

    public String getqAudio4() {
        if(qAudio4 != null) {
            return qAudio4;
        }
        else {
            return null;
        }
    }

    public String getqImg1() {
        if(qImg1 != null) {
            return qImg1;
        }
        else {
            return null;
        }
    }

    public String getqImg2() {
        if(qImg2 != null) {
            return qImg2;
        }
        else {
            return null;
        }
    }

    public String getqImg3() {
        if(qImg3 != null) {
            return qImg3;
        }
        else {
            return null;
        }
    }
    public String getqImg4() {
        if(qImg4 != null) {
            return qImg4;
        }
        else {
            return null;
        }
    }

    public String getQuestionText() {
        if(questionText != null) {
            return questionText;
        }
        else {
            return null;
        }
    }

}
