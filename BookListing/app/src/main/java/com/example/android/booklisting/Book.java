package com.example.android.booklisting;

import android.util.Log;

/**
 * Created by john on 01.11.2016.
 */

public class Book {
    private String mAuthorName;
    private String mTitleName;
    private String mUrl;
    private static final String LOG_TAG = Book.class.getName();

    public Book(String authorName, String titleName, String url) {
        this.mAuthorName = authorName;
        this.mTitleName = titleName;
        this.mUrl = url;
        Log.v(LOG_TAG, "constructor");
    }

    public String getAuthorName() {
        Log.v(LOG_TAG, "getAuthorName");
        return mAuthorName;
    }

    public String getTitleName() {
        Log.v(LOG_TAG, "getTitleName");
        return mTitleName;
    }

    public String getUrl() {
        Log.v(LOG_TAG, "getUrl");
        return mUrl;
    }
}
