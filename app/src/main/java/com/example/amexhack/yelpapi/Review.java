package com.example.amexhack.yelpapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amexhack.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Review extends AppCompatActivity {

    private String BASE_URL = "https://api.yelp.com/v3/businesses/";
    private String TAG = "MainActivity";
    private String API_KEY = "LWAv17INzFEQdx0yv-OisJHiqJnSRtRA3nHr_IJVfNgPWCWY0D_8y85km7Ifo6NtWROpFfk8JLAKFxbqS6gyXaFWMPvrTrAg8Gh9SwEXYnixrKzUwqMaar5g9yNRX3Yx";

    private String id;

    private SharedPreferences viewThisPage;

    SharedPreferences.Editor viewThisPageEditor;

    private static final String myPreference = "viewThisPage";




    ArrayList<YelpReview> reviews = new ArrayList<>();

    LinearLayout reviewScroller;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        reviewScroller = findViewById(R.id.reviewsScrollView);

        viewThisPage = this.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        viewThisPageEditor = viewThisPage.edit();

        String word = viewThisPage.getString(myPreference, "");
        System.out.println(word);

        if (word.contains("Department of Coffee")){
            id = "7fnq7-pePPEM2WaZ4CLg0g";
        }
        else{
            id = viewThisPage.getString(myPreference, "");
        }







        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(BASE_URL+id+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YelpService yelpService2 = retrofit2.create(YelpService.class);

        yelpService2.searchReviews("Bearer "+ API_KEY).enqueue(new Callback<YelpReviewsOfPlace>() {

            @Override
            public void onResponse(Call<YelpReviewsOfPlace> call, Response<YelpReviewsOfPlace> response) {
                Log.i(TAG, "onResponse "+response);
                YelpReviewsOfPlace result = response.body();

                if (result == null){
                    Log.w(TAG, "Did not receive valid response");
                    return;

                }

                reviews.addAll(result.getReviews());
                System.out.println(reviews);

                for (YelpReview review: reviews){

                    System.out.println(review.getUser().getName()+ review.getRating()+ review.getText());
                    addReview(review.getUser().getName(), review.getRating(), review.getText());
                }

                //For each review call the inflator function and display on screen
            }

            @Override public void onFailure(Call<YelpReviewsOfPlace> call, Throwable t) {
                Log.i(TAG, "onFailure "+t);

            }
        });



    }


    public void addReview(String username, int rating, String description){

        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.row_reviews, null);

        final TextView usernameTextView = addView.findViewById(R.id.username);
        usernameTextView.setText(username);

        final TextView ratingTextView = addView.findViewById(R.id.rating);
        ratingTextView.setText(Integer.toString(rating)+" / 5");

        final TextView descriptionTextView = addView.findViewById(R.id.reviewText);
        descriptionTextView.setText(description);

        reviewScroller.addView(addView);

    }

    //[YelpRestaurant(name=Department of Coffee and Social Affairs, rating=4.5, price=££, numReviews=70, distanceInMeters=2005.3115588771366, imageUrl=https://s3-media1.fl.yelpcdn.com/bphoto/W_xUKBTvCBaAlWlj7UjbUg/o.jpg, categories=[YelpCategory(title=Coffee & Tea)], location=YelpLocation(address=14-16 Leather Lane)), YelpRestaurant(name=Department of Coffee and Social Affairs, rating=3.5, price=££, numReviews=18, distanceInMeters=186.67773358478217, imageUrl=https://s3-media1.fl.yelpcdn.com/bphoto/vxod0lhDEdivwTs_vrelNw/o.jpg, categories=[YelpCategory(title=Coffee & Tea)], location=YelpLocation(address=3 Lowndes Court)), YelpRestaurant(name=Department of Coffee and Social Affairs, rating=4.5, price=£, numReviews=13, distanceInMeters=4238.654752021698, imageUrl=https://s3-media2.fl.yelpcdn.com/bphoto/n2BlUbkG30tJ_t8oQU_a6Q/o.jpg, categories=[YelpCategory(title=Coffee & Tea)], location=YelpLocation(address=6 Lamb Street)), YelpRestaurant(name=Department of Coffee and Social Affairs, rating=4.0, price=null, numReviews=2, distanceInMeters=219.85711235350539, imageUrl=https://s3-media2.fl.yelpcdn.com/bphoto/ryGnCysaCv-Xptoxx9FxmQ/o.jpg, categories=[YelpCategory(title=Coffee & Tea)], location=YelpLocation(address=30 Warwick Street)), YelpRestaurant(name=Ozone Coffee, rating=4.5, price=££, numReviews=189, distanceInMeters=3655.552919913525, imageUrl=https://s3-media2.fl.yelpcdn.com/bphoto/RSDAEdz-0KLGj8aRmqXPWQ/o.jpg, categories=[YelpCategory(title=Coffee & Tea), YelpCategory(title=Breakfast & Brunch)], location=YelpLocation(address=11 Leonard Street)), YelpRestaurant(name=Tonic Coffee Bar, rating=4.0, price=££, numReviews=5, distanceInMeters=188.70831287232065, imageUrl=https://s3-media1.fl.yelpcdn.com/bphoto/fF24gMaCA7ZhnwrsFeWLAA/o.jpg, categories=[YelpCategory(title=Coffee & Tea)], location=YelpLocation(address=15 Sherwood Street)), YelpRestaurant(name=Prufrock Coffee, rating=4.0, price=££, numReviews=90, distanceInMeters=1991.0474125039245, imageUrl=https://s3-media4.fl.yelpcdn.com/bphoto/JdIpUpy8TrYo42xBatSwgg/o.jpg, categories=[YelpCategory(title=Coffee & Tea)], location=YelpLocation(address=23-25 Leather Lane)), YelpRestaurant(name=Notes, rating=4.0, price=££, numReviews=102, distanceInMeters=730.8861345602843, imageUrl=https://s3-media3.fl.yelpcdn.com/bphoto/2n4_s5u-BgN4-zKSUhwamw/o.jpg, categories=[YelpCategory(title=Coffee & Tea), YelpCategory(title=Patisserie/Cake Shop), YelpCategory(title=Wine Bars)], location=YelpLocation(address=31 St Martin's Lane)), YelpRestaurant(name=Arro Coffee - The Temple of Coffee, rating=4.5, price=£, numReviews=13, distanceInMeters=3600.8360260651657, imageUrl=https://s3-media4.fl.yelpcdn.com/bphoto/CMZhp_IWzTHT9Cr9cLxyTw/o.jpg, categories=[YelpCategory(title=Coffee & Tea), YelpCategory(title=Italian)], location=YelpLocation(address=77 Bishop's Bridge Road)), YelpRestaurant(name=Mamma's Goodies, rating=4.0, price=null, numReviews=1, distanceInMeters=4268.895588088336, imageUrl=https://s3-media3.fl.yelpcdn.com/bphoto/ysEYysVLjOLmcWiwp7762w/o.jpg, categories=[YelpCategory(title=Italian), YelpCategory(title=Desserts), YelpCategory(title=Coffee & Tea)], location=YelpLocation(address=Lamb Street)), YelpRestaurant(name=Costa Coffee, rating=3.0, price=£££, numReviews=5, distanceInMeters=2269.0729692517516, imageUrl=https://s3-media4.fl.yelpcdn.com/bphoto/ZO_FPS86CDitBqAEOTmvwQ/o.jpg, categories=[YelpCategory(title=Coffee & Tea)], location=YelpLocation(address=46 Cowcross St)), YelpRestaurant(name=Konditor & Cook, rating=4.5, price=££, numReviews=35, distanceInMeters=2009.750808419612, imageUrl=https://s3-media4.fl.yelpcdn.com/bphoto/Z0QgVzsI7CGUWtjQ7_VeSQ/o.jpg, categories=[YelpCategory(title=Bakeries), YelpCategory(title=Coffee & Tea), YelpCategory(title=Patisserie/Cake Shop)], location=YelpLocation(address=22 Cornwall Road)), YelpRestaurant(name=Nude Espresso, rating=4.0, price=££, numReviews=88, distanceInMeters=4450.421304868383, imageUrl=https://s3-media3.fl.yelpcdn.com/bphoto/6oywUE1tqElHKzQoTpRfqg/o.jpg, categories=[YelpCategory(t
}