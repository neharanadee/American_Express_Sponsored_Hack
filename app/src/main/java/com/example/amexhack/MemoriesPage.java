package com.example.amexhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class MemoriesPage extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor sharedPreferencesEditor;

    private static final String descriptionOfImages = "descriptions";

    private static final String imagesPath = "images";

    TextView empty;

    LinearLayout scroller;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories);



        sharedPreferences = this.getSharedPreferences(descriptionOfImages, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        scroller = (LinearLayout)findViewById(R.id.imageScroller);



        if (sharedPreferences.contains(imagesPath)){
            String text = sharedPreferences.getString(descriptionOfImages, "");
            String imagePath = sharedPreferences.getString(imagesPath,"");

            System.out.println(text);
            System.out.println(imagePath);

            String[] descriptionArray = text.split("_");
            String[] imagesArray = imagePath.split(";");
            System.out.println(descriptionArray);
            System.out.println(imagesArray);


            for (int x =0 ; x<imagesArray.length; x++) {

                System.out.println(imagesArray[x]);
                System.out.println(descriptionArray[x]);
                createImageView(imagesArray[x], descriptionArray[x]);
            }




        }

        else{
            empty = findViewById(R.id.onlyifempty);
            empty.setText("Add some memories to see them here!!!");
        }



    }

    public void createImageView(String imagesPath, String imageDescription){
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.row_memory, null);

        File f = new File(imagesPath);
        Uri contentUri = Uri.fromFile(f);
        ImageView image = new ImageView(this);
        image.setImageURI(contentUri);

        boolean portrait = false;
        if (image.getHeight() > image.getWidth()){
            portrait  = true;
        }



        final ImageView imageView = addView.findViewById(R.id.imageTakenBefore);
        imageView.setImageURI(contentUri);

        if (portrait){
            imageView.setRotation(90);
        }



        final TextView textView = addView.findViewById(R.id.textDescription);
        textView.setText(imageDescription);

        scroller.addView(addView);





    }




}