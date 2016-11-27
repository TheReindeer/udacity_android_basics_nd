package com.system.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news){
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }

        News currentNews = getItem(position);

        String section = currentNews.getmArticleSection();
        TextView sectionView = (TextView) listItemView.findViewById(R.id.news_section);
        sectionView.setText(section);

        String title = currentNews.getmArticleTitle();
        TextView titleView = (TextView) listItemView.findViewById(R.id.news_title);
        titleView.setText(title);

        String author = currentNews.getmArticleAuthor();
        TextView authorView = (TextView) listItemView.findViewById(R.id.news_author);
        authorView.setText(author);

        //Format date and time
        String dateObject = currentNews.getmArticleDate();
        String[] formattedDateAndTime = formatDateAndTime(dateObject);

        TextView dateView = (TextView) listItemView.findViewById(R.id.news_date);
        dateView.setText(formattedDateAndTime[0]);

        TextView timeView = (TextView) listItemView.findViewById(R.id.news_time);
        String timeFormattedString = removeLastChar(formattedDateAndTime[1]);
        timeView.setText(timeFormattedString);

        return listItemView;
    }

    //Format the date and time in order to be displayed separately
    private String[] formatDateAndTime(String unformString){
        String separatorString = "T";
        String date = "";
        String time = "";
        if (unformString.contains(separatorString)){
            String[] parts = unformString.split(separatorString);
            date = parts[0];
            time = parts[1];
        }
        String[] formattedString = {date, time};
        return formattedString;
    }

    //Remove the 'Z' char at the end of time value
    private String removeLastChar(String string){
        if (string != null && string.length() > 0 && string.charAt(string.length()-1) == 'Z'){
            string = string.substring(0, string.length()-1);
        }
        return string;
    }
}
