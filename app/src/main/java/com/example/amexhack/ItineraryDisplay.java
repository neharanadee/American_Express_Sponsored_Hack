package com.example.amexhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.amexhack.MainActivity;
import com.example.amexhack.ItineraryDisplay;
import com.example.amexhack.InformationAboutPlace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ItineraryDisplay extends AppCompatActivity {

    private Button viewMoreButton;

    private SharedPreferences viewThisPage;

    SharedPreferences.Editor viewThisPageEditor;

    private static final String myPreference = "viewThisPage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_display);

        viewThisPage = this.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        viewThisPageEditor = viewThisPage.edit();




        viewMoreButton = findViewById(R.id.viewMoreInfoButton);
        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItineraryDisplay();
            }
        });


    }

    public void openItineraryDisplay(){
        saveInfoToPreferences();

        Intent intent = new Intent(this, InformationAboutPlace.class);
        startActivity(intent);
    }

    public void saveInfoToPreferences(){
        String variableThatContainsAnswers = "Department of Coffee";
        viewThisPageEditor.putString(myPreference, variableThatContainsAnswers);
        //CALL THE FUNCTION THAT RETURNS THE BEST LOCATIONS THAT THE USER SHOULD VISIT

        viewThisPageEditor.commit();
    }


}