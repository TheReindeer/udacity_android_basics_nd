package com.example.android.tourguideapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by john on 26.10.2016.
 */

public class ConstructorsAdapter extends ArrayAdapter<Constructors> {

    private int mResourceId;

    public ConstructorsAdapter(Activity context, ArrayList<Constructors> constructors, int mResourceId){
        super(context, 0, constructors);
        this.mResourceId = mResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        Constructors currentConstructor = getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentConstructor.getmName());

        TextView descriptionTextView = (TextView) listItemView.findViewById(R.id.description_text_view);
        descriptionTextView.setText(currentConstructor.getmDescription());

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.imageView);
        if (currentConstructor.hasImage()){
            imageView.setImageResource(currentConstructor.getmImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        ImageView imageArrow = (ImageView) listItemView.findViewById(R.id.image_arrow);
        if(currentConstructor.hasImageArrow()) {
            imageArrow.setImageResource(currentConstructor.getmImageArrowId());
            imageArrow.setVisibility(View.VISIBLE);
        } else {
            imageArrow.setVisibility(View.GONE);
        }

        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mResourceId);
        textContainer.setBackgroundColor(color);

        return listItemView;
    }
}
