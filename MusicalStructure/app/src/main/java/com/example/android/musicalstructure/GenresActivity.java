package com.example.android.musicalstructure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GenresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

        Button btnAll = (Button) findViewById(R.id.btn_genres_to_all);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenresActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnArtists = (Button) findViewById(R.id.btn_genres_to_artists);
        btnArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenresActivity.this, ArtistsActivity.class);
                startActivity(intent);
            }
        });

        TextView tvToGenreSelected = (TextView) findViewById(R.id.textview_genres);
        tvToGenreSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenresActivity.this, GenreSelectedActivity.class);
                startActivity(intent);
            }
        });
    }
}
