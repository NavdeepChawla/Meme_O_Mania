package com.adgvit.meme_o_mania;

import android.os.Parcel;
import android.os.Parcelable;

public class questionObject implements Parcelable {

    private String question;
    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private int correct;
    private String img;

    public questionObject(String question,String a1,String a2,String a3,String a4,int correct, String img){

        this.question = question;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
        this.correct = correct;
        this.img = img;
    }

    public questionObject(){

    }

    protected questionObject(Parcel in) {
        question = in.readString();
        a1 = in.readString();
        a2 = in.readString();
        a3 = in.readString();
        a4 = in.readString();
        correct = in.readInt();
        img = in.readString();
    }

    public static final Creator<questionObject> CREATOR = new Creator<questionObject>() {
        @Override
        public questionObject createFromParcel(Parcel in) {
            return new questionObject(in);
        }

        @Override
        public questionObject[] newArray(int size) {
            return new questionObject[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public String getA1() {
        return a1;
    }

    public String getA2() {
        return a2;
    }

    public String getA3() {
        return a3;
    }

    public String getA4() {
        return a4;
    }

    public int getCorrect() {
        return correct;
    }

    public String getImg() {
        return img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(a1);
        parcel.writeString(a2);
        parcel.writeString(a3);
        parcel.writeString(a4);
        parcel.writeInt(correct);
        parcel.writeString(img);
    }
}
