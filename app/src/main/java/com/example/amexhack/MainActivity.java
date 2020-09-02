package com.example.amexhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amexhack.MainActivity;
import com.example.amexhack.ItineraryDisplay;
import com.google.android.material.tabs.TabItem;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MainActivity extends AppCompatActivity {

    private Button nextPageButton;

    private LinearLayout scrollableLayout;

    private Button addActivityButton;

    private int activityNo = 1;

    ArrayList<String> items = new ArrayList<>();

    SpinnerDialog spinnerDialog;

    ArrayList<String> activities = new ArrayList<>();

    private SharedPreferences planResults;

    SharedPreferences.Editor planResultsEditor;

    private static final String myPreference = "finalLocations";

    Button memoriesPage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items.add("Dining: Bar/Pub");
        items.add("Dining: Cafe/Restaurant");
        items.add("Dining: Chinese");
        items.add("Dining: Coffee Shop");
        items.add("Dining: Dessert");
        items.add("Dining: French");
        items.add("Dining: Indian");
        items.add("Dining: Italian");
        items.add("Dining: Japanese");
        items.add("Dining: Korean");
        items.add("Dining: Mexican");
        items.add("Dining: Restaurant");
        items.add("Dining: Scandinavian");
        items.add("Dining: Spanish");
        items.add("Dining: Thai");

        items.add("Entertainment: Aquarium");
        items.add("Entertainment: Art Galleries");
        items.add("Entertainment: Bowling");
        items.add("Entertainment: Cinema");
        items.add("Entertainment: Clubs");
        items.add("Entertainment: Theatre");

        items.add("Services: Beauty Salon");
        items.add("Services: Dry Cleaner");
        items.add("Services: Eyebrow Bar");
        items.add("Services: Gym");
        items.add("Services: Hairdresser");
        items.add("Services: Nail Bar");
        items.add("Services: Perfumes");
        items.add("Services: Skin Clinic");
        items.add("Services: Tailor");
        items.add("Services: Tanning");
        items.add("Services: Tattoo");
        items.add("Services: Waxing");

        items.add("Shopping: Arts, Books and Supplies");
        items.add("Shopping: Clothing");
        items.add("Shopping: Coffee");
        items.add("Shopping: Department Store");
        items.add("Shopping: Electronics");
        items.add("Shopping: General Food");
        items.add("Shopping: Japanese Food");
        items.add("Shopping: Korean Food");
        items.add("Shopping: Medicine");
        items.add("Shopping: Sports");



        planResults = this.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        planResultsEditor = planResults.edit();






        nextPageButton = findViewById(R.id.buttonToItinerary);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItineraryDisplay();
            }
        });

        scrollableLayout = findViewById(R.id.inScrollable);

        addActivityButton= findViewById(R.id.addNewActivity);
        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewActivityTextView();
            }
        });

        memoriesPage = findViewById(R.id.memories);
        memoriesPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReviews();
            }
        });

    }

    public void openReviews(){
        Intent intent = new Intent(this, Reviews.class);
        startActivity(intent);
    }

    public void openItineraryDisplay(){
        saveInfoToPreferences();
        Intent intent = new Intent(this, ItineraryDisplay.class);
        startActivity(intent);
    }

    public void saveInfoToPreferences(){
        String allActivities ="";

        int n = 0;


        String variableThatContainsAnswers = "";
        planResultsEditor.putString(myPreference, variableThatContainsAnswers);
        //CALL THE FUNCTION THAT RETURNS THE BEST LOCATIONS THAT THE USER SHOULD VISIT
        planResultsEditor.commit();






    }

    public void createNewActivityTextView(){

        final String[] chosen = new String[1];
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.row_activity, null);
        final TextView name = addView.findViewById(R.id.textNumberActivity);
        name.setText(Integer.toString(activityNo));

        final TextView activity = addView.findViewById(R.id.ActivityInputField);

        spinnerDialog = new SpinnerDialog(MainActivity.this, items, "Select Activity");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                activity.setText(item);

            }
        });





        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();

            }
        });

        activities.add(activity.getText().toString());




        activityNo +=1 ;
        scrollableLayout.addView(addView);



    }



}