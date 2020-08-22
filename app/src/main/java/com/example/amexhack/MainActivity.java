package com.example.amexhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.amexhack.MainActivity;
import com.example.amexhack.ItineraryDisplay;

public class MainActivity extends AppCompatActivity {

    private Button nextPageButton;

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
    }

    public void openItineraryDisplay(){
        Intent intent = new Intent(this, ItineraryDisplay.class);
        startActivity(intent);
    }
}