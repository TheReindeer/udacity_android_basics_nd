package com.example.android.scorekeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int scorePlayerOne = 0;
    int scorePlayerTwo = 0;
    int framesPlayerOne = 0;
    int framesPlayerTwo = 0;
    int redBallsLeft = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayScorePlayerOne(scorePlayerOne);
        displayScorePlayerTwo(scorePlayerTwo);
        displayFramesPlayerOne(framesPlayerOne);
        displayFramesPlayerTwo(framesPlayerTwo);
        displayRedBallsLeft(redBallsLeft);
    }

    //Methods to display scores
    public void displayScorePlayerOne(int score){
        TextView scoreView = (TextView) findViewById(R.id.score_player_one);
        scoreView.setText(String.valueOf(score));
    }

    public void displayScorePlayerTwo(int score){
        TextView scoreView = (TextView) findViewById(R.id.score_player_two);
        scoreView.setText(String.valueOf(score));
    }

    public void displayFramesPlayerOne(int frames){
        TextView scoreView = (TextView) findViewById(R.id.frames_player_one);
        scoreView.setText(String.valueOf(frames));
    }

    public void displayFramesPlayerTwo(int frames){
        TextView scoreView = (TextView) findViewById(R.id.frames_player_two);
        scoreView.setText(String.valueOf(frames));
    }

    public void displayRedBallsLeft(int reds){
        TextView redBallsLeftOne = (TextView) findViewById(R.id.red_balls_left_player_one);
        TextView redBallsLeftTwo = (TextView) findViewById(R.id.red_balls_left_player_two);
        redBallsLeftOne.setText(String.valueOf(reds));
        redBallsLeftTwo.setText(String.valueOf(reds));
    }

    //Methods to add points/frames for "Player One"
    public void addOnePointForPlayerOne(View v){
        if (redBallsLeft > 0) {
            scorePlayerOne += 1;
            displayScorePlayerOne(scorePlayerOne);
            decreaseRedBallsLeft();
        } else {
            Toast.makeText(this, "Congratulations! \n All red balls are in their holes!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addTwoPointsForPlayerOne(View v){
        scorePlayerOne += 2;
        displayScorePlayerOne(scorePlayerOne);
    }

    public void addThreePointsForPlayerOne(View v){
        scorePlayerOne += 3;
        displayScorePlayerOne(scorePlayerOne);
    }

    public void addFourPointsForPlayerOne(View v){
        scorePlayerOne += 4;
        displayScorePlayerOne(scorePlayerOne);
    }

    public void addFivePointsForPlayerOne(View v){
        scorePlayerOne += 5;
        displayScorePlayerOne(scorePlayerOne);
    }

    public void addSixPointsForPlayerOne(View v){
        scorePlayerOne += 6;
        displayScorePlayerOne(scorePlayerOne);
    }

    public void addSevenPointsForPlayerOne(View v){
        scorePlayerOne += 7;
        displayScorePlayerOne(scorePlayerOne);
    }

    public void addFrameForPlayerOne(View v){
        framesPlayerOne += 1;
        displayFramesPlayerOne(framesPlayerOne);
    }

    //Methods to add points/frames for "Player Two"
    public void addOnePointForPlayerTwo(View v){
        if (redBallsLeft > 0) {
            scorePlayerTwo += 1;
            displayScorePlayerTwo(scorePlayerTwo);
            decreaseRedBallsLeft();
        }
    }

    public void addTwoPointsForPlayerTwo(View v){
        scorePlayerTwo += 2;
        displayScorePlayerTwo(scorePlayerTwo);
    }

    public void addThreePointsForPlayerTwo(View v){
        scorePlayerTwo += 3;
        displayScorePlayerTwo(scorePlayerTwo);
    }

    public void addFourPointsForPlayerTwo(View v){
        scorePlayerTwo += 4;
        displayScorePlayerTwo(scorePlayerTwo);
    }

    public void addFivePointsForPlayerTwo(View v){
        scorePlayerTwo += 5;
        displayScorePlayerTwo(scorePlayerTwo);
    }

    public void addSixPointsForPlayerTwo(View v){
        scorePlayerTwo += 6;
        displayScorePlayerTwo(scorePlayerTwo);
    }

    public void addSevenPointsForPlayerTwo(View v){
        scorePlayerTwo += 7;
        displayScorePlayerTwo(scorePlayerTwo);
    }

    public void addFrameForPlayerTwo(View v){
        framesPlayerTwo += 1;
        displayFramesPlayerTwo(framesPlayerTwo);
    }

    //Method to decrease the number of red balls left
    public void decreaseRedBallsLeft(){
        redBallsLeft -= 1;
        displayRedBallsLeft(redBallsLeft);
    }

    //Method to reset scores
    public void resetScores(View v){
        scorePlayerOne = 0;
        scorePlayerTwo = 0;
        framesPlayerOne = 0;
        framesPlayerTwo = 0;
        redBallsLeft = 15;
        displayScorePlayerOne(scorePlayerOne);
        displayScorePlayerTwo(scorePlayerTwo);
        displayFramesPlayerOne(framesPlayerOne);
        displayFramesPlayerTwo(framesPlayerTwo);
        displayRedBallsLeft(redBallsLeft);
    }
}
