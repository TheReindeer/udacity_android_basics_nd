package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by john on 01.11.2016.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private final String LOG_TAG = BookLoader.class.getName();
    private String mUrl;

    public BookLoader(Context context, String url){
        super(context);
        mUrl = url;
        Log.v(LOG_TAG, "constructor");
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v(LOG_TAG, "onStartLoading");
    }

    @Override
    public List<Book> loadInBackground(){
    Log.v(LOG_TAG, "loadInBackgroundCalled");
        if (mUrl == null){
            return null;
        }

        List<Book> books = BookQueryUtils.fetchBookData(mUrl);
        Log.v(LOG_TAG, "loadInBackgroundFinished");
        return books;
    }
}
