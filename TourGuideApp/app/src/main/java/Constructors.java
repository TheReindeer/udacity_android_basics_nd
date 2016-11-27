package com.example.android.tourguideapp;

/**
 * Created by john on 26.10.2016.
 */

public class Constructors {

    private int mNameResourceId;
    private int mDescriptionResourceId;
    private static final int NO_IMAGE = -1;
    private int mImageResourceId = NO_IMAGE;
    private static final int NO_ARROW = -1;
    private int mImageArrowId = NO_ARROW;
    private int mAudioResourceId;

    public Constructors(int mName, int mDescription, int mImageResourceId){
        this.mNameResourceId = mName;
        this.mDescriptionResourceId = mDescription;
        this.mImageResourceId = mImageResourceId;
    }

    public Constructors(int mName, int mDescription, int mImageResourceId, int mImageArrowId, int mAudioResourceId){
        this.mNameResourceId = mName;
        this.mDescriptionResourceId = mDescription;
        this.mImageResourceId = mImageResourceId;
        this.mImageArrowId = mImageArrowId;
        this.mAudioResourceId = mAudioResourceId;
    }

    public int getmName() {
        return mNameResourceId;
    }

    public int getmDescription() {
        return mDescriptionResourceId;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }

    public int getmAudioResourceId() {
        return mAudioResourceId;
    }

    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE;
    }

    public int getmImageArrowId() {
        return mImageArrowId;
    }

    public boolean hasImageArrow() {
        return mImageArrowId != NO_ARROW;
    }
}
