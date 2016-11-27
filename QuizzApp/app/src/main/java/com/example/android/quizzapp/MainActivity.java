package com.example.android.quizzapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //EditTexts for Business Name and Location
    @BindView(R.id.etBusinessName) EditText etBusinessName;
    @BindView(R.id.etBusinessLocation) EditText etBussinesLocation;

    //RadioButtons for Question 1
    @BindView(R.id.rbTypeRestaurant) RadioButton rbTypeRestaurant;
    @BindView(R.id.rbTypePub) RadioButton rbTypePub;
    @BindView(R.id.rbTypeClub) RadioButton rbTypeClub;

    //RadioButtons for Question 2
    @BindView(R.id.rbFloorArea50) RadioButton rbFloorArea50;
    @BindView(R.id.rbFloorArea100) RadioButton rbFloorArea100;
    @BindView(R.id.rbFloorArea150) RadioButton rbFloorArea150;
    @BindView(R.id.rbFloorArea200) RadioButton rbFloorArea200;

    //RadioButtons for Question 3
    @BindView(R.id.rbNumberPersons200) RadioButton rbNumberPersons200;
    @BindView(R.id.rbNumberPersons400) RadioButton rbNumberPersons400;
    @BindView(R.id.rbNumberPersons600) RadioButton rbNumberPersons600;
    @BindView(R.id.rbNumberPersons800) RadioButton rbNumberPersons800;

    //CheckBoxes
    @BindView(R.id.cbIsFireProtected) CheckBox cbIsFireProtected;
    @BindView(R.id.cbIsFireVerified) CheckBox cbIsFireVerified;

    //EditText number of emergency exits
    @BindView(R.id.etEmergencyExitNumbers) EditText etEmergencyExitNumbers;

    //Buttons Submit and Send Result (mail)
    @BindView(R.id.btnSubmit) Button btnSubmit;
    @BindView(R.id.btnSendResult) Button btnSendResult;

    //Final scoreto be displayed
    public int finalScore = 0;
    public String fullReport = "FULL REPORT: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSubmit)
    public void BtnSubmitClick(){
        boolean areFieldsCompleted = checkAllFields();
        if (areFieldsCompleted) {
            submitFinalResult();
            finalScore = 0;
        } else {
            Toast.makeText(this, "Please complete all the fields.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnSendResult)
    public void BtnSendResultClick(){
        boolean areFieldsCompleted = checkAllFields();
        if (areFieldsCompleted){
            sendResultToMail();
            fullReport = "FULL REPORT: "; //resetting only after it is send to mail. That means if the user submits 10 times and doesn't send results, he will have 10 reports.
        } else {
            Toast.makeText(this, "Please complete all the fields.", Toast.LENGTH_SHORT).show();
        }
    }

    public void submitFinalResult(){
        //Creating a par tof the full report
        fullReport += "\n\n" + String.valueOf(etBusinessName.getText());
        fullReport += ", " + String.valueOf(etBussinesLocation.getText());

        String businessType = "";
        if (rbTypeRestaurant.isChecked()) {
            businessType = "Restaurant";
        } else if (rbTypeClub.isChecked()) {
            businessType = "Club";
        } else if (rbTypePub.isChecked()) {
            businessType = "Pub";
        }
        fullReport += ", " + businessType;

        //RadioGroup floor area - giving values in the selected interval to meet the conditions for the calculatePrice() method
        int squareMetters = 0;
        if (rbFloorArea50.isChecked()){
            squareMetters = 25;
        } else if (rbFloorArea100.isChecked()) {
            squareMetters = 75;
        } else if (rbFloorArea150.isChecked()) {
            squareMetters = 125;
        } else if (rbFloorArea200.isChecked()) {
            squareMetters = 175;
        }

        //RadioGroup maximum number of persons
        int maxNumberPersons = 0;
        if (rbNumberPersons200.isChecked()) {
            maxNumberPersons = 200;
        } else if (rbNumberPersons400.isChecked()) {
            maxNumberPersons = 400;
        } else if (rbNumberPersons600.isChecked()) {
            maxNumberPersons = 600;
        } else if (rbNumberPersons800.isChecked()) {
            maxNumberPersons = 800;
        }

        //CheckBoxes for fire sytems and CheckBox for exit numbers
        boolean isFireSystemProtected = cbIsFireProtected.isChecked();
        boolean isFireSystemVerified = cbIsFireVerified.isChecked();
        String numberOfExitsValue = String.valueOf(etEmergencyExitNumbers.getText());
        int numberOfExits = Integer.valueOf(numberOfExitsValue);

        //Calling calculation method and displaying the Toast message
        calculateFinalScore(squareMetters, maxNumberPersons, isFireSystemProtected, isFireSystemVerified, numberOfExits);
        if (finalScore == 100) {
            Toast.makeText(this, "Congratulations! Your final score is: 100%.\n " +
                    "Your bussiness mets the required specifications!", Toast.LENGTH_SHORT).show();
            fullReport += "\n\tCongratulations! Your bussiness mets the required specifications!";
        } else {
            Toast.makeText(this, "Congratulations! Your final score is: " + finalScore +
                        "%.\nFor a full report press Send Result Button.", Toast.LENGTH_LONG).show();
        }
    }

    private void calculateFinalScore(int floorArea, int maximumPersons, boolean fireProtection, boolean fireSystemVerification, int numberOfExits){
        //Calculations of final score depending on floor area, maximum number of persons and number of emergency exits
        if (floorArea >= 0 && floorArea <= 50){
            if(maximumPersons >= 0 && maximumPersons <= 200){
                if (numberOfExits == 4){
                    finalScore += 60;
                } else {
                    finalScore += 40;
                    fullReport += "\n\tYour answer regarding the minimum number of emergency exits is incorrect. " +
                            "According to the law you MUST have at least 1 emergency exit for every 50 persons";
                }
            } else {
                fullReport += "\n\tYour floor area is too small for the number of persons specified. " +
                        "According to the law you MUST have at least 4 square metters of space for each person";
                if (numberOfExits == 4){
                    finalScore += 40;
                } else {
                    fullReport += "\n\tYour answer regarding the minimum number of emergency exits is incorrect. " +
                            "According to the law you MUST have at least 1 emergency exit for every 50 persons";
                }
            }
        }

        if (floorArea > 50  && floorArea <= 100){
            if(maximumPersons >= 0 && maximumPersons <= 400){
                if (numberOfExits == 8){
                    finalScore += 60;
                } else {
                    finalScore += 40;
                    fullReport += "\n\tYour answer regarding the minimum number of emergency exits is incorrect. " +
                            "According to the law you MUST have at least 1 emergency exit for every 50 persons";
                }
            } else {
                fullReport += "\n\tYour floor area is too small for the number of persons specified. " +
                        "According to the law you MUST have at least 4 square metters of space for each person";
                if (numberOfExits == 8){
                    finalScore += 40;
                } else {
                    fullReport += "\n\tYou do not have enough emergency exits for the number of persons specified. " +
                            "According to the law you MUST have at least 1 emergency exit for every person";
                }
            }
        }

        if (floorArea > 100  && floorArea <= 150){
            if(maximumPersons >= 0 && maximumPersons <= 600){
                if (numberOfExits == 12){
                    finalScore += 60;
                } else {
                    finalScore += 40;
                    fullReport += "\n\tYour answer regarding the minimum number of emergency exits is incorrect. " +
                            "According to the law you MUST have at least 1 emergency exit for every 50 persons";
                }
            } else {
                fullReport += "\n\tYour floor area is too small for the number of persons specified. " +
                        "According to the law you MUST have at least 4 square metters of space for each person";
                if (numberOfExits == 12){
                    finalScore += 40;
                } else {
                    fullReport += "\n\tYour answer regarding the minimum number of emergency exits is incorrect. " +
                            "According to the law you MUST have at least 1 emergency exit for every person";
                }
            }
        }

        if (floorArea > 150  && floorArea <= 200){
            if(maximumPersons >= 0 && maximumPersons <= 800){
                if (numberOfExits == 16){
                    finalScore += 60;
                } else {
                    finalScore += 40;
                    fullReport += "\n\tYour answer regarding the minimum number of emergency exits is incorrect. " +
                            "According to the law you MUST have at least 1 emergency exit for every 50 persons";
                }
            } else {
                fullReport += "\n\tYour floor area is too small for the number of persons specified. " +
                        "According to the law you MUST have at least 4 square metters of space for each person";
                if (numberOfExits == 16){
                    finalScore += 40;
                } else {
                    fullReport += "\n\tYour answer regarding the minimum number of emergency exits is incorrect. " +
                            "According to the law you MUST have at least 1 emergency exit for 50 persons";
                }
            }
        }

        //Calculations of final score depending on fire systems checkboxes
        if (fireProtection) {
            finalScore += 20;
        } else {
            fullReport += "\n\tYou MUST have a fire protection system installed.";
        }

        if (fireSystemVerification) {
            finalScore += 20;
        } else {
            fullReport += "\n\tYour fire protection system MUST be verified by the local empowered authorities.";
        }
    }

    public void sendResultToMail(){
        String subjectMessage = "Full report";
        composeEmail(subjectMessage, fullReport);
    }
    public void composeEmail(String subject, String text){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public boolean checkAllFields(){
        if(!etBusinessName.getText().toString().trim().equals("")){
            if(!etBussinesLocation.getText().toString().trim().equals("")){
                if(rbTypeRestaurant.isChecked() || rbTypePub.isChecked() || rbTypeClub.isChecked()){
                    if(rbFloorArea50.isChecked() || rbFloorArea100.isChecked() || rbFloorArea150.isChecked() || rbFloorArea200.isChecked()){
                        if(rbNumberPersons200.isChecked() || rbNumberPersons400.isChecked() || rbNumberPersons600.isChecked() ||rbNumberPersons800.isChecked()){
                            if(!etEmergencyExitNumbers.getText().toString().trim().equals("")){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
