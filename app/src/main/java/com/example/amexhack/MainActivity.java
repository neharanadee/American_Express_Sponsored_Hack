package com.example.amexhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amexhack.MainActivity;
import com.example.amexhack.ItineraryDisplay;

public class MainActivity extends AppCompatActivity {

    private Button nextPageButton;

    private LinearLayout scrollableLayout;

    private Button addActivityButton;

    private int activityNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    public void openItineraryDisplay(){
        Intent intent = new Intent(this, ItineraryDisplay.class);
        startActivity(intent);
    }

    public void createNewActivityTextView(){
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.row_activity, null);
        final TextView name = addView.findViewById(R.id.textNumberActivity);
        name.setText(Integer.toString(activityNo));
        activityNo +=1 ;
        scrollableLayout.addView(addView);



    }
}