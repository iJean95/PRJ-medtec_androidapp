package com.echopen.asso.echopen;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class SequenceImage implements Parcelable{

    public ArrayList<Bitmap> mCapturedSequence;
    public SequenceImage()
    {
        mCapturedSequence = new ArrayList<Bitmap>();
    }

    public static final Creator<SequenceImage> CREATOR = new Creator<SequenceImage>() {
        @Override
        public SequenceImage createFromParcel(Parcel in) {
            return new SequenceImage(in);
        }

        @Override
        public SequenceImage[] newArray(int size) {
            return new SequenceImage[size];
        }
    };

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mCapturedSequence);
    }
    private SequenceImage(Parcel in){
        mCapturedSequence = new ArrayList<Bitmap>();
        in.readTypedList(mCapturedSequence, Bitmap.CREATOR);
    }
}

