package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListingActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookListingActivity.class.getName();
    private static String GAPI_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static String completeUrl;
    private BookAdapter mBookAdapter;
    private static final int BOOK_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private EditText mKeywordEditText;
    private ImageButton mSearchButtonImage;
    private boolean hasConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_listing);
        Log.v(LOG_TAG, "onCreate");

        final ListView booksListView = (ListView) findViewById(R.id.list_books);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(LOG_TAG, "bookClickListener");
                Book currentBook = mBookAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);
            }
        });

        mKeywordEditText = (EditText) findViewById(R.id.et_search);
        mSearchButtonImage = (ImageButton) findViewById(R.id.btn_search);
        mSearchButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(LOG_TAG, "searchClickListener");
                hasConnection = testConnection();
                if (hasConnection){
                    Log.v(LOG_TAG, "searchClickListener true");
                    String suffixUrl = mKeywordEditText.getText().toString();
                    completeUrl = GAPI_REQUEST_URL + suffixUrl;
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(BOOK_LOADER_ID, null, BookListingActivity.this).forceLoad();
                    mBookAdapter = new BookAdapter(BookListingActivity.this, new ArrayList<Book>());
                    booksListView.setAdapter(mBookAdapter);
                    mBookAdapter.notifyDataSetChanged();
                    loaderManager.initLoader(BOOK_LOADER_ID, null, BookListingActivity.this);
                    Log.v(LOG_TAG, "URL: "+completeUrl);
                } else {
                    Log.v(LOG_TAG, "searchClickListener false");
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    mEmptyStateTextView.setText(R.string.no_connection);
                }
            }
        });
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader");
        return new BookLoader(this, completeUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        Log.v(LOG_TAG, "onLoadFinished");
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_books);

        mBookAdapter.clear();

        if (data != null && !data.isEmpty()){
            mBookAdapter.addAll(data);
        }
        mBookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.v(LOG_TAG, "onLoaderReset");
        mBookAdapter.clear();
    }

    private boolean testConnection(){
        Log.v(LOG_TAG, "testConnection");
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        } else {
            return false;
        }
    }
}
