package com.example.android.musicalstructure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ArtistsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);

        Button btnAll = (Button) findViewById(R.id.btn_artists_to_all);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArtistsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnGenres = (Button) findViewById(R.id.btn_artists_to_genres);
        btnGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArtistsActivity.this, GenresActivity.class);
                startActivity(intent);
            }
        });

        TextView tvToArtistSelected = (TextView) findViewById(R.id.textview_artists);
        tvToArtistSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArtistsActivity.this, ArtistSelectedActivity.class);
                startActivity(intent);
            }
        });
    }
}
