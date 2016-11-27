package com.example.android.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by john on 01.11.2016.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookAdapter.class.getName();

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
        Log.v(LOG_TAG, "constructor");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(LOG_TAG, "getView");
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }

        Book curentBook = getItem(position);

        TextView authorView = (TextView) listItemView.findViewById(R.id.tv_author);
        authorView.setText(curentBook.getAuthorName());

        TextView titleView = (TextView) listItemView.findViewById(R.id.tv_title);
        titleView.setText(curentBook.getTitleName());

        return listItemView;
    }
}
