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
    String radioAudio;
    String radioText1;
    String radioText2;
    String radioText3;
    String radioText4;
    String img1Text;
    String img2Text;
    String img3Text;
    String img4Text;
    String letter;
    String radioImg;

    int _1pair1;
    int _2pair1;
    int _1pair2;
    int _2pair2;
    int _1pair3;
    int _2pair3;
    int _1pair4;
    int _2pair4;
    int _1pair5;
    int _2pair5;
    int _1pair6;
    int _2pair6;

    String _1pair1String;
    String _2pair1String;
    String _1pair2String;
    String _2pair2String;
    String _1pair3String;
    String _2pair3String;
    String _1pair4String;
    String _2pair4String;
    String _1pair5String;
    String _2pair5String;
    String _1pair6String;
    String _2pair6String;

    public int get1pair1() {
        if(_1pair1 != -1) {
            return _1pair1;
        }
        else {
            return -1;
        }
    }

    public int get2pair1() {
        if(_2pair1 != -1) {
            return _2pair1;
        }
        else {
            return -1;
        }
    }

    public int get1pair2() {
        if(_1pair2 != -1) {
            return _1pair2;
        }
        else {
            return -1;
        }
    }

    public int get2pair2() {
        if(_2pair2 != -1) {
            return _2pair2;
        }
        else {
            return -1;
        }
    }

    public int get1pair3() {
        if(_1pair3 != -1) {
            return _1pair3;
        }
        else {
            return -1;
        }
    }

    public int get2pair3() {
        if(_2pair3 != -1) {
            return _2pair3;
        }
        else {
            return -1;
        }
    }

    public int get1pair4() {
        if(_1pair4 != -1) {
            return _1pair4;
        }
        else {
            return -1;
        }
    }

    public int get2pair4() {
        if(_2pair4 != -1) {
            return _2pair4;
        }
        else {
            return -1;
        }
    }

    public int get1pair5() {
        if(_1pair5 != -1) {
            return _1pair5;
        }
        else {
            return -1;
        }
    }

    public int get2pair5() {
        if(_2pair5 != -1) {
            return _2pair5;
        }
        else {
            return -1;
        }
    }

    public int get1pair6() {
        if(_1pair6 != -1) {
            return _1pair6;
        }
        else {
            return -1;
        }
    }

    public int get2pair6() {
        if(_2pair6 != -1) {
            return _2pair6;
        }
        else {
            return -1;
        }
    }

    public String get1pair1String() {
        if(_1pair1String != null) {
            return _1pair1String;
        }
        else {
            return null;
        }
    }

    public String get2pair1String() {
        if(_2pair1String != null) {
            return _2pair1String;
        }
        else {
            return null;
        }
    }

    public String get1pair2String() {
        if(_1pair2String != null) {
            return _1pair2String;
        }
        else {
            return null;
        }
    }

    public String get2pair2String() {
        if(_2pair2String != null) {
            return _2pair2String;
        }
        else {
            return null;
        }
    }

    public String get1pair3String() {
        if(_1pair3String != null) {
            return _1pair3String;
        }
        else {
            return null;
        }
    }

    public String get2pair3String() {
        if(_2pair3String != null) {
            return _2pair3String;
        }
        else {
            return null;
        }
    }

    public String get1pair4String() {
        if(_1pair4String != null) {
            return _1pair4String;
        }
        else {
            return null;
        }
    }

    public String get2pair4String() {
        if(_2pair4String != null) {
            return _2pair4String;
        }
        else {
            return null;
        }
    }

    public String get1pair5String() {
        if(_1pair5String != null) {
            return _1pair5String;
        }
        else {
            return null;
        }
    }

    public String get2pair5String() {
        if(_2pair5String != null) {
            return _2pair5String;
        }
        else {
            return null;
        }
    }

    public String get1pair6String() {
        if(_1pair6String != null) {
            return _1pair6String;
        }
        else {
            return null;
        }
    }

    public String get2pair6String() {
        if(_2pair6String != null) {
            return _2pair6String;
        }
        else {
            return null;
        }
    }
    //
    public String getradioImg() {
        if(radioImg != null) {
            return radioImg;
        }
        else {
            return null;
        }
    }

    public String getLetter() {
        if(letter != null) {
            return letter;
        }
        else {
            return null;
        }
    }

    public String getimg4Text() {
        if(img4Text != null) {
            return img4Text;
        }
        else {
            return null;
        }
    }

    public String getimg3Text() {
        if(img3Text != null) {
            return img3Text;
        }
        else {
            return null;
        }
    }

    public String getimg2Text() {
        if(img2Text != null) {
            return img2Text;
        }
        else {
            return null;
        }
    }

    public String getimg1Text() {
        if(img1Text != null) {
            return img1Text;
        }
        else {
            return null;
        }
    }

    public String getradioAudio() {
        if(radioAudio != null) {
            return radioAudio;
        }
        else {
            return null;
        }
    }

    public String getradioText1() {
        if(radioText1 != null) {
            return radioText1;
        }
        else {
            return null;
        }
    }

    public String getradioText2() {
        if(radioText2 != null) {
            return radioText2;
        }
        else {
            return null;
        }
    }

    public String getradioText3() {
        if(radioText3 != null) {
            return radioText3;
        }
        else {
            return null;
        }
    }

    public String getradioText4() {
        if(radioText4 != null) {
            return radioText4;
        }
        else {
            return null;
        }
    }

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
            case "1pair1":
                return get1pair1();
            case "2pair1":
                return get2pair1();
            case "1pair2":
                return get1pair2();
            case "2pair2":
                return get2pair2();
            case "1pair3":
                return get1pair3();
            case "2pair3":
                return get2pair3();
            case "1pair4":
                return get1pair4();
            case "2pair4":
                return get2pair4();
            case "1pair5":
                return get1pair5();
            case "2pair5":
                return get2pair5();
            case "1pair6":
                return get1pair6();
            case "2pair6":
                return get2pair6();
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
            case "radioAudio":
                return getradioAudio();
            case "radioText1":
                return getradioText1();
            case "radioText2":
                return getradioText2();
            case "radioText3":
                return getradioText3();
            case "radioText4":
                return getradioText4();
            case "img1Text":
                return getimg1Text();
            case "img2Text":
                return getimg2Text();
            case "img3Text":
                return getimg3Text();
            case "img4Text":
                return getimg4Text();
            case "letter":
                return getLetter();
            case "radioImg":
                return getradioImg();
            case "1pair1":
                return get1pair1String();
            case "2pair1":
                return get2pair1String();
            case "1pair2":
                return get1pair2String();
            case "2pair2":
                return get2pair2String();
            case "1pair3":
                return get1pair3String();
            case "2pair3":
                return get2pair3String();
            case "1pair4":
                return get1pair4String();
            case "2pair4":
                return get2pair4String();
            case "1pair5":
                return get1pair5String();
            case "2pair5":
                return get2pair5String();
            case "1pair6":
                return get1pair6String();
            case "2pair6":
                return get2pair6String();
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
