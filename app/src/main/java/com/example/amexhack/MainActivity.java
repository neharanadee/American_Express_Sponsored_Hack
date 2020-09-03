package com.example.amexhack;
import android.os.Bundle;
import com.example.amexhack.OptimalDistance.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.amexhack.Testing2;

import com.example.amexhack.MainActivity;
import com.example.amexhack.ItineraryDisplay;
import com.google.android.material.tabs.TabItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MainActivity extends AppCompatActivity {

    private Button nextPageButton;
    private TextView title;
    public String TAG = "OKAY A";
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

                try {
                    openItineraryDisplay();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        title = findViewById(R.id.textViewTitleWhatToDo);
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

    public void openItineraryDisplay() throws IOException {
        Testing2 t = new Testing2();
        String result = t.Hello();
        title.setText(result);
        saveInfoToPreferences();
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);

//        Intent intent = new Intent(this, ItineraryDisplay.class);
//        startActivity(intent);

    }

    public void saveInfoToPreferences() throws IOException {
        String allActivities ="";

        int n = 0;
        String variableThatContainsAnswers = getBestLocations();
        //sendRequest("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=dining&keyword=Bar/Pub&key=AIzaSyATPwWeg_lhIP1Lh3Mls3U_WSaY3AI21G4");
        Log.d(TAG, "variable that contains answs: " + variableThatContainsAnswers);
        System.out.println(variableThatContainsAnswers);
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
    public String getBestLocations() throws IOException {
        StringBuilder sb = new StringBuilder();
        try {

            OptimalDistance optimalDistance = new OptimalDistance();

            double longi = 0.1378;
            double latit = 51.5134;

            ArrayList<String> sampleToDo = new ArrayList<String>();
            sampleToDo.add("Dining: Bar/Pub");
            sampleToDo.add("Services: Beauty Salon");
            sampleToDo.add("Services: Nail Bar");

            ArrayList<OptimalDistance.Place> result = optimalDistance.findAllPlaces(sampleToDo, latit, longi);

            for(OptimalDistance.Place element : result){
                if(element != null) {
                    sb.append(element.name);
                    //System.out.println(element.getName());
                }
                else{
                    sb.append("null");
                    //System.out.println("null");
                }
                sb.append(",");
            }

        } catch (Exception ex){
            System.out.println("error type" + ex.getClass());
            System.out.println("error stack" + ex.getStackTrace());
            System.out.println("err " + ex.getMessage());
        }
        return sb.toString();
    }

    public  JSONObject sendRequest(String url) {

        StringBuilder stringBuilder = new StringBuilder();


        RequestQueue volleyObject = Volley.newRequestQueue(this);
        final JSONObject[] result = {null};
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result[0] = response;
                        Log.e("Rest Response", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("On error", error.toString());
            }
        });
        volleyObject.add(objectRequest);
        return result[0];
    }

    private OptimalDistance.Place getTopN(double latitude, double longitude, String category, String subcategory, int radius, String fullFormForCategory) throws JSONException {
        System.out.println("STARTING!!!!!");
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius="+radius+"&type="+category+"&keyword="+subcategory+"&key="+api_key+"";
        JSONArray allPlaces = (JSONArray)sendRequest(url).get("results");
        System.out.println("ENTERING LOOP!!!!!");


        for (int i = 0; i < allPlaces.length() ; i++){

            System.out.println("LOOP!!!!!");
            JSONObject currentElement = (JSONObject) allPlaces.get(i);
            String name = (String) currentElement.get("name");

            if(this.allCategoriesWithPlaces.get(fullFormForCategory).contains(name) == true) {
                System.out.println("match");
                JSONObject geometry = (JSONObject) currentElement.get("geometry");
                JSONObject location = (JSONObject) geometry.get("location");
                Double long_current = (Double) location.get("lng");
                Double lat_current = (Double) location.get("lat");
                OptimalDistance.Place newPlace = new OptimalDistance.Place(name, lat_current, long_current);
                System.out.println(name + " : " + lat_current + " : " + long_current);
                return newPlace;
            }
        }

        return null;
    }


}