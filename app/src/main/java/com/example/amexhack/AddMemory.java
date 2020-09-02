package com.example.amexhack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMemory extends AppCompatActivity {

    Button addPhoto;

    public static int REQUEST_IMAGE_TO_TAKE = 1;

    ImageView imageView;

    String currentPhotoPath;

    Button save;

    EditText description;

    private SharedPreferences sharedPreferences;

    SharedPreferences.Editor sharedPreferencesEditor;

    private static final String descriptionOfImages = "descriptions";

    private static final String imagesPath = "images";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        System.out.println(storageDir);

        sharedPreferences = this.getSharedPreferences(descriptionOfImages, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();


        addPhoto = findViewById(R.id.takePhoto);
        addPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                takePictureIntent();

            }
        });

        imageView = findViewById(R.id.imageOfPhoto);

        save = findViewById(R.id.save);



        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveMemory();
                goBack();


            }
        });

        description = findViewById(R.id.description);

    }




    public void takePictureIntent(){
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (imageIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.amexhack.fileprovider",
                        photoFile);
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(imageIntent, REQUEST_IMAGE_TO_TAKE);

            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_TO_TAKE && resultCode == RESULT_OK) {
            galleryAddPic();


            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);

            imageView.setImageURI(contentUri);

        }



    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }



    public void saveMemory(){

        String descriptions = "";
        String images = "";

        if (sharedPreferences.contains(descriptionOfImages)){
            descriptions = sharedPreferences.getString(descriptionOfImages,"");
            images= sharedPreferences.getString(imagesPath,"");
            descriptions += "_"+ description.getText().toString() ;
            images += ";"+ currentPhotoPath;
        }
        else{
            descriptions = description.getText().toString();
            images = currentPhotoPath;
        }




        sharedPreferencesEditor.putString(descriptionOfImages, descriptions);
        sharedPreferencesEditor.putString(imagesPath, images);
        sharedPreferencesEditor.commit();


    }

    public void goBack(){
        Intent intent = new Intent(this, InformationAboutPlace.class);
        startActivity(intent);

    }
}