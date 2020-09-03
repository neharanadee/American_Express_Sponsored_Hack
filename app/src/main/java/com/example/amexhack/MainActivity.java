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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

            double longi = 0.1378;
            double latit = 51.5134;

            ArrayList<String> sampleToDo = new ArrayList<String>();
            sampleToDo.add("Dining: Bar/Pub");
            sampleToDo.add("Services: Beauty Salon");
            sampleToDo.add("Services: Nail Bar");

            ArrayList<Place> result = findAllPlaces(sampleToDo, latit, longi);

            for(Place element : result){
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

    class Place{
        public String name = "";
        double latitude = 0;
        double longitude = 0;

        public Place(String name, double latitude, double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = name;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getName() {
            return name;
        }
    }



    String api_key = "AIzaSyATPwWeg_lhIP1Lh3Mls3U_WSaY3AI21G4";

    HashMap<String, HashSet<String>> allCategoriesWithPlaces = new HashMap<String, HashSet<String>>();

    public void constructHashMap() throws IOException, ParseException {
        try {

            String JSONString = "{\n" +
                    "  \"Dining: Coffee Shop\": [\"Department Of Coffee And Social Affair\", \"Sacred Cafe\", \"Cafe Concerto\"],\n" +
                    "  \"Dining: Bar/Pub\": [\"The Yard\", \"Ku Bar\", \"Bar Italia\", \"Circa\", \"Mr Fogg’s Residence\", \"Whyte and Brown\", \"68 and Boston\", \"The French House\", \"Two Floors\", \"Mr Fogg's Tavern\"],\n" +
                    "  \"Dining: Cafe/Restaurant\":[ \"My Place Soho\",\"Kaffeine\", \"Piggys Piccadilly\", \"Kricket Soho\", \"Quo Vadis\"] ,\n" +
                    "  \"Dining: Dessert\":[\"Happy Lemon\"], \n" +
                    "  \"Dining: French\": [\"Pierre Victoire\"], \n" +
                    "  \"Dining: Indian\": [\"Kanishka Restaurant\", \"The Kati Roll Company\"], \n" +
                    "  \"Dining: Italian\": [\"Bancone\", \"Icco Pizza\", \"L Antica Pizzeria Da Michele Soho\"], \n" +
                    "  \"Dining: Japanese\": [\"Kanada-Ya - Panton Street\"], \n" +
                    "  \"Dining: Korean\": [\"Jinjuu Soho\"], \n" +
                    "  \"Dining: Mexican\": [\"Chipotle mexican grill\"], \n" +
                    "  \"Dining: Restaurant\": [], \n" +
                    "  \"Dining: Scandinavian\": [\"Scanidnavian kitchen\"], \n" +
                    "  \"Dining: Spanish\": [\"Barrafina\", \"Lobos Soho\"],\n" +
                    "  \"Dining: Thai\": [\"Ye Grapes\"], \n" +
                    "  \"Dining: Chinese\": [\"Wok to Walk\",\"Golden Dragon\", \"Gerrad's Corner Restaurant\" ],\n" +
                    "  \"Entertainment: Aquarium\": [\"Sea Life London Aquarium\"], \n" +
                    "  \"Entertainment: Art Galleries\":[\"National Portrait Gallery\"], \n" +
                    "  \"Entertainment: Bowling\":[\"Bloomsbury Bowling\"], \n" +
                    "  \"Entertainment: Cinema\":[\"Renoir Cinema\"], \n" +
                    "  \"Entertainment: Clubs\":[\"H Club London\"], \n" +
                    "  \"Entertainment: Theatre\":[\"The Adephi Threatre Company\"],\n" +
                    "  \"Services: Beauty Salon\":[\"Luxury Beauty Room\", \"May Beauty\", \"Beauty Box Soho\", \"Snail & Hare Beauty Bar\", \"Hannah London Clinics\", \"Le Fix\", \"Radiance Beauty Clinic\", \"Browhaus\", \"Singhar Hair & Beauty\", \"Nails & Brows\", \"Hiromiyoshi Hair & Beauty\"], \n" +
                    "  \"Services: Dry Cleaner\":[\"Celebrity Dry Cleaners\", \"Seven Dials Dry Cleaners\", \"City Centre Dry Cleaners\", \"City Centre Dry Cleaners\"], \n" +
                    "  \"Services: Eyebrow Bar\":[\"Lash Bar\"], \n" +
                    "  \"Services: Gym\":[\"F45 Oxford Circus\",\"Psycle\"], \n" +
                    "  \"Services: Hairdresser\":[\"Eton Crop Hairdressing\", \"Long Hair In London\", \"Eton Hair\", \"Tower Salon\", \"Pankhurst\", \"Billy & Bo\", \"The One Hairdressing & Beauty\"], \n" +
                    "  \"Services: Nail Bar\":[\"Beauty Box Nail Bar\", \"Star Nails\", \"Coco & Rachel\", \"London Grace\", \"Nails & Brows\", \"Baroque Hair & Nails\", \"Dryby\"], \n" +
                    "  \"Services: Perfumes\":[\"Roja Parfums\"], \n" +
                    "  \"Services: Skin Clinic\": [\"Linia Skin Clinic\", \"Clinica Fiore\", \"Face Clinic Soho\", \"Skin Perfection\"],\n" +
                    "  \"Services: Tailor\": [\"Norton & Sons\"], \n" +
                    "  \"Services: Tanning\": [\"The Mayfair Tanning\"], \n" +
                    "  \"Services: Tattoo\": [\"The Circle\"], \n" +
                    "  \"Services: Waxing\": [\"Ministry Of Waxing\", \"The Mayfair Tanning\", \"Luxury Beauty Room\"], \n" +
                    "  \"Shopping: Arts, Books and Supplies\": [\"Paperchase Eccom\"], \n" +
                    "  \"Shopping: Clothing\": [\"Fenwick\"], \n" +
                    "  \"Shopping: Electronics\": [\"Apple Regent Centre\"], \n" +
                    "  \"Shopping: Medicine\": [\"Boots The Chemist\"], \n" +
                    "  \"Shopping: Sports\": [\"Sports Direct\", \"Lilywhites\"], \n" +
                    "  \"Shopping: Department Store\": [\"House of Fraser\"]\n" +
                    "  }";
            JSONObject jsonObject = new JSONObject(JSONString);



            String[] allCategoryNames = new String[]{"Dining: Coffee Shop", "Dining: Bar/Pub", "Dining: Cafe/Restaurant", "Dining: Dessert", "Dining: French", "Dining: Indian", "Dining: Italian", "Dining: Japanese", "Dining: Korean", "Dining: Mexican", "Dining: Restaurant", "Dining: Scandinavian", "Dining: Spanish", "Dining: Thai", "Dining: Chinese", "Entertainment: Aquarium", "Entertainment: Art Galleries", "Entertainment: Bowling", "Entertainment: Cinema", "Entertainment: Clubs", "Entertainment: Theatre", "Services: Beauty Salon", "Services: Dry Cleaner", "Services: Eyebrow Bar", "Services: Gym", "Services: Hairdresser", "Services: Nail Bar", "Services: Perfumes", "Services: Skin Clinic", "Services: Tailor", "Services: Tanning", "Services: Tattoo", "Services: Waxing", "Shopping: Arts, Books and Supplies", "Shopping: Clothing", "Shopping: Electronics", "Shopping: Medicine", "Shopping: Sports", "Shopping: Department Store"};

            for(String category : allCategoryNames) {
                JSONArray Dining_Bar_Pub = (JSONArray) jsonObject.get(category);

                this.allCategoriesWithPlaces.put(category, new HashSet<String>());
                for(int i = 0; i < Dining_Bar_Pub.length() ; i++){
                    String currentPlace = (String) Dining_Bar_Pub.get(i);
                    this.allCategoriesWithPlaces.get(category).add(currentPlace);
                }
            }

            for(String element : this.allCategoriesWithPlaces.keySet()){
                System.out.println(element);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Place> findAllPlaces(ArrayList<String> categories, double latitude, double longitude) throws JSONException, IOException, ParseException {
        constructHashMap();
        ArrayList<Place> result = new ArrayList<Place>();
        double startLatitude = latitude;
        double startLongitude = longitude;
        for(String element : categories){

            String[] elementBreakdown = element.split(":");
            String category = elementBreakdown[0].trim();
            String subcategory = elementBreakdown[1].trim().replaceAll("\\s+", "");
            Place currentResult = getTopN(startLatitude, startLongitude, category, subcategory, 10000, element);
            if (currentResult != null){
                startLatitude = currentResult.latitude;
                startLongitude = currentResult.longitude;
            }
            System.out.println("THIS SHOULD PRINT PLACE NAME" + currentResult.name);
            result.add(currentResult);
        }

        return result;

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

    private Place getTopN(double latitude, double longitude, String category, String subcategory, int radius, String fullFormForCategory) throws JSONException {
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
                Place newPlace = new Place(name, lat_current, long_current);
                System.out.println(name + " : " + lat_current + " : " + long_current);
                return newPlace;
            }
        }

        return null;
    }


}