package com.example.amexhack.yelpapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.amexhack.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Review extends AppCompatActivity {

    private String BASE_URL = "https://api.yelp.com/v3/";
    private String TAG = "MainActivity";
    private String API_KEY = "LWAv17INzFEQdx0yv-OisJHiqJnSRtRA3nHr_IJVfNgPWCWY0D_8y85km7Ifo6NtWROpFfk8JLAKFxbqS6gyXaFWMPvrTrAg8Gh9SwEXYnixrKzUwqMaar5g9yNRX3Yx";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YelpService yelpService = retrofit.create(YelpService.class);
        yelpService.searchRestaurants("Bearer "+ API_KEY,"Department of Coffee", "London").enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i(TAG, "onResponse "+response);

            }

            @Override public void onFailure(Call<Object> call, Throwable t) {
                Log.i(TAG, "onFailure "+t);

            }
        });


    }
}