package com.example.amexhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class InformationAboutPlace extends AppCompatActivity {

    ImageSlider imageSlider;

    List<SlideModel> slideModelList = new ArrayList<>();

    Button reviewButton;

    Button addToMemories;

    public static int REQUEST_IMAGE_TO_TAKE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_about_place);

        imageSlider = findViewById(R.id.imageSlider);


        slideModelList.add(new SlideModel(R.drawable.coffee_image,"DEPARTMENT OF COFFEE AND SOCIAL AFFAIRS", ScaleTypes.CENTER_CROP));
        slideModelList.add(new SlideModel(R.drawable.coffee_image_2,"DEPARTMENT OF COFFEE AND SOCIAL AFFAIRS", ScaleTypes.CENTER_CROP));
        slideModelList.add(new SlideModel(R.drawable.coffee_image_3,"DEPARTMENT OF COFFEE AND SOCIAL AFFAIRS", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModelList, ScaleTypes.CENTER_CROP);

        reviewButton = findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReviewPages();
            }
        });


        addToMemories = findViewById(R.id.addToMemories);

        addToMemories.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openMemoryPage();

            }
        });




    }


    public void openItineraryDisplay(){
        Intent intent = new Intent(this, MemoriesPage.class);
        startActivity(intent);

    }

    public void openReviewPages(){
        Intent intent;
    }

    public void openMemoryPage(){
        Intent changePage = new Intent(this, AddMemory.class);
        startActivity(changePage);

    }
}