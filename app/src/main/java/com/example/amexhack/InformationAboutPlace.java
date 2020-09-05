package com.example.amexhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.amexhack.yelpapi.Review;
import com.example.amexhack.yelpapi.YelpBusiness;
import com.example.amexhack.yelpapi.YelpReview;
import com.example.amexhack.yelpapi.YelpReviewsOfPlace;
import com.example.amexhack.yelpapi.YelpService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class InformationAboutPlace extends AppCompatActivity {

    ImageSlider imageSlider;

    List<SlideModel> slideModelList = new ArrayList<>();

    Button reviewButton;

    Button addToMemories;

    TextView ratings;

    TextView prices;

    TextView openingTimes;

    String id;



    private SharedPreferences viewThisPage;

    SharedPreferences.Editor viewThisPageEditor;

    private static final String myPreference = "viewThisPage";

    private static final String locations = "itineraryLocations";

    private String BASE_URL = "https://api.yelp.com/v3/businesses/";

    private String API_KEY = "LWAv17INzFEQdx0yv-OisJHiqJnSRtRA3nHr_IJVfNgPWCWY0D_8y85km7Ifo6NtWROpFfk8JLAKFxbqS6gyXaFWMPvrTrAg8Gh9SwEXYnixrKzUwqMaar5g9yNRX3Yx";

    private String TAG = "InfoAboutPlace";

    public static int REQUEST_IMAGE_TO_TAKE = 1;
    //= "7fnq7-pePPEM2WaZ4CLg0g"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_about_place);

        imageSlider = findViewById(R.id.imageSlider);

        viewThisPage = this.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        viewThisPageEditor = viewThisPage.edit();

        ratings = findViewById(R.id.ratingsOfThisBusiness);

        openingTimes = findViewById(R.id.openingTimesOfBusiness);

        prices = findViewById(R.id.priceOfThisBusiness);

        String word = viewThisPage.getString(myPreference, "");
        System.out.println(word);


        if (word.equals("Department of Coffee")){
            id = "7fnq7-pePPEM2WaZ4CLg0g";


//            System.out.println("IN DEPT OF COFFEE");
//
//            slideModelList.add(new SlideModel(R.drawable.coffee_image, "DEPARTMENT OF COFFEE AND SOCIAL AFFAIRS", ScaleTypes.CENTER_CROP));
//        slideModelList.add(new SlideModel(R.drawable.coffee_image_2, "DEPARTMENT OF COFFEE AND SOCIAL AFFAIRS", ScaleTypes.CENTER_CROP));
//        slideModelList.add(new SlideModel(R.drawable.coffee_image_3, "DEPARTMENT OF COFFEE AND SOCIAL AFFAIRS", ScaleTypes.CENTER_CROP));
//
//        imageSlider.setImageList(slideModelList, ScaleTypes.CENTER_CROP);
    }



        else {
            id = viewThisPage.getString(myPreference, "");
        }


            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            YelpService yelpService2 = retrofit2.create(YelpService.class);

            yelpService2.searchBusinessDetails(id, "Bearer " + API_KEY).enqueue(new Callback<YelpBusiness>() {

                @Override
                public void onResponse(Call<YelpBusiness> call, Response<YelpBusiness> response) {
                    Log.i(TAG, "onResponse " + response);
                    YelpBusiness result = response.body();

                    if (result == null) {
                        Log.w(TAG, "Did not receive valid response");
                        return;

                    }
                    System.out.println(result);
                    String name = result.getName();
                    List<String> photos = result.getPhotos();
                    System.out.println(photos);
                    Double rating = result.getRating();
                    String price = result.getPrice();
                    System.out.println("hmm");
                    String open = result.getHours().get(0).getOpen().get(0).getStart();
                    String end = result.getHours().get(0).getOpen().get(0).getEnd();
                    int index = 1;
                    String openString = open.substring(0, index + 1)
                            + ":"
                            + open.substring(index + 1);

                    String endString = end.substring(0, index + 1)
                            + ":"
                            + end.substring(index + 1);


                    ratings.setText(rating.toString());

                    prices.setText(price);

                    //openingTimes.setText("Opens at: "+open+"to"+ end);
                    List<SlideModel> slideModelList = new ArrayList<>();

                    for (String image: photos){
                        slideModelList.add(new SlideModel(image, name, ScaleTypes.CENTER_CROP));
                    }

                    imageSlider.setImageList(slideModelList, ScaleTypes.CENTER_CROP);


                    //For each review call the inflator function and display on screen

                    openingTimes.setText("Opens: "+openString + " to "+endString);
                }

                @Override
                public void onFailure(Call<YelpBusiness> call, Throwable t) {
                    Log.i(TAG, "onFailure " + t);

                }
            });







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

    public void setOnToScreen(String name, List<String> photos, Double rating, String pricing, String open, String end){





    }


    public void openItineraryDisplay(){
        Intent intent = new Intent(this, MemoriesPage.class);
        startActivity(intent);

    }

    public void openReviewPages(){

        Intent intent = new Intent(this, Review.class);
        startActivity(intent);
    }

    public void openMemoryPage(){
        Intent changePage = new Intent(this, AddMemory.class);
        startActivity(changePage);

    }







}