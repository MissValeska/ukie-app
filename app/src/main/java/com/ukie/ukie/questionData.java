package com.ukie.ukie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Valeska on 07/06/2017.
 */

public class questionData implements Parcelable {

    /*int QuestionCount;
    int index;
    int progress;*/
    String conj;
    String conjtype;
    String infin;
    String type;
    int correctAnswer;
    String[] qAudio;
    String[] qImg;
    String questionText;

    protected questionData(Parcel in) {
        /*QuestionCount = in.readInt();
        index = in.readInt();
        progress = in.readInt();*/
        conj = in.readString();
        conjtype = in.readString();
        infin = in.readString();
        type = in.readString();
        correctAnswer = in.readInt();
        qAudio = in.createStringArray();
        qImg = in.createStringArray();
        questionText = in.readString();
    }

    public static final Creator<questionData> CREATOR = new Creator<questionData>() {
        @Override
        public questionData createFromParcel(Parcel in) {
            return new questionData(in);
        }

        @Override
        public questionData[] newArray(int size) {
            return new questionData[size];
        }
    };

    public questionData() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /*dest.writeInt(QuestionCount);
        dest.writeInt(index);
        dest.writeInt(progress);*/
        dest.writeString(conj);
        dest.writeString(conjtype);
        dest.writeString(infin);
        dest.writeString(type);
        dest.writeInt(correctAnswer);
        dest.writeStringArray(qAudio);
        dest.writeStringArray(qImg);
        dest.writeString(questionText);
    }
}
