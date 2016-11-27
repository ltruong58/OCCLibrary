package com.example.longtruong.cs273.ltruong58.occlibrary;

/**
 * Created by Long Truong on 11/27/2016.
 */

public class Room {
    private int mId;
    private String mName;
    private String mDescription;
    private int mCapacity;

    public Room(int mId, String mName, String mDescription, int mCapacity) {
        this.mId = mId;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mCapacity = mCapacity;
    }

    public Room( String mName, String mDescription, int mCapacity) {
        this(-1, mName, mDescription, mCapacity);
    }

    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getmCapacity() {
        return mCapacity;
    }

    public void setmCapacity(int mCapacity) {
        this.mCapacity = mCapacity;
    }
}
