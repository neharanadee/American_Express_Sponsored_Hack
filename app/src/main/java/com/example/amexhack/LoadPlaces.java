package com.example.amexhack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LoadPlaces extends AppCompatActivity {

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Place> placeList;
    private RecyclerView.Adapter adapter;

    public static ArrayList<String> listOfAllTheCategories;

    public static double startLatitude;
    public static double startLongitude;

    public HashMap<String, HashSet<String>> allCategoriesWithPlaces = new HashMap<String, HashSet<String>>();

    private Button getMapsButton;

    String api_key = "AIzaSyATPwWeg_lhIP1Lh3Mls3U_WSaY3AI21G4";

//    private String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=15000&type=Dining&keyword=Indian&key=AIzaSyATPwWeg_lhIP1Lh3Mls3U_WSaY3AI21G4";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_places);

        createHashMap();

        mList = findViewById(R.id.main_list);

        placeList = new ArrayList<>();
        adapter = new PlaceAdapter(getApplicationContext(),placeList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        for(String element : listOfAllTheCategories){
            String[] elementBreakdown = element.split(":");
            String category = elementBreakdown[0].trim();
            String subcategory = elementBreakdown[1].trim().replaceAll("\\s+", "");
            String urlVal = getTopNURL(startLatitude, startLongitude, category, subcategory, 15000, element);
            getData(urlVal,element) ;
        }




        getMapsButton = findViewById(R.id.buttonToMap);
        getMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    openMapsDisplay();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }


    private void createHashMap(){
        try {

            String json = "{\n" +
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

            JSONObject jsonObject = new JSONObject(json);


            String[] allCategoryNames = new String[]{"Dining: Coffee Shop", "Dining: Bar/Pub", "Dining: Cafe/Restaurant", "Dining: Dessert", "Dining: French", "Dining: Indian", "Dining: Italian", "Dining: Japanese", "Dining: Korean", "Dining: Mexican", "Dining: Restaurant", "Dining: Scandinavian", "Dining: Spanish", "Dining: Thai", "Dining: Chinese", "Entertainment: Aquarium", "Entertainment: Art Galleries", "Entertainment: Bowling", "Entertainment: Cinema", "Entertainment: Clubs", "Entertainment: Theatre", "Services: Beauty Salon", "Services: Dry Cleaner", "Services: Eyebrow Bar", "Services: Gym", "Services: Hairdresser", "Services: Nail Bar", "Services: Perfumes", "Services: Skin Clinic", "Services: Tailor", "Services: Tanning", "Services: Tattoo", "Services: Waxing", "Shopping: Arts, Books and Supplies", "Shopping: Clothing", "Shopping: Electronics", "Shopping: Medicine", "Shopping: Sports", "Shopping: Department Store"};


            for(String category : allCategoryNames) {
                JSONArray currentArray = (JSONArray) jsonObject.get(category);

                allCategoriesWithPlaces.put(category, new HashSet<String>());
                System.out.println("category " + category);

                for (int i = 0; i < currentArray.length(); i++) {
//                    System.out.println()
                    String currentPlace = (String)currentArray.get(i);
//                    String currentPlace = (String) currentArray.get(i);
                    allCategoriesWithPlaces.get(category).add(currentPlace);
                }
            }

            for(String element : allCategoriesWithPlaces.keySet()){
                System.out.println(element);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openMapsDisplay() throws IOException {

        MapActivity.AllPlaces = placeList;
        System.out.println("ALL PLACE LIST " + placeList.size());
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);


    }

    private String getTopNURL(double latitude, double longitude, String category, String subcategory, int radius, String fullFormForCategory)  {
        System.out.println("STARTING!!!!!");
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=" + radius + "&type=" + category + "&keyword=" + subcategory + "&key=" + api_key + "";
        return url;
    }


    private void getData(String url, String fullFormForCategory) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null , new Response.Listener<JSONObject>() {
            boolean added = false;
            @Override
            public void onResponse(JSONObject response) {
//                for (int i = 0; i < response.length(); i++) {
                    try {
                        System.out.println("resposne " + response);
                        JSONObject responseJSON = (JSONObject) response;
                        JSONArray results = (JSONArray) response.get("results");
                        for(int i = 0; i < results.length(); i++) {
                            JSONObject currentElement = (JSONObject) results.get(i);
                            System.out.println("results " + responseJSON);
                            if (currentElement.length() > 0) {
                                String name = (String) currentElement.get("name");
                                System.out.println("fullFormForCategory " + fullFormForCategory);
                                if (allCategoriesWithPlaces.get(fullFormForCategory).contains(name) == true) {
                                    added = true;
                                    JSONObject geometry = (JSONObject) currentElement.get("geometry");
                                    JSONObject location = (JSONObject) geometry.get("location");
                                    Double long_current = (Double) location.get("lng");
                                    Double lat_current = (Double) location.get("lat");

                                    System.out.println("lat_current " + lat_current);
                                    Place place = new Place();
                                    place.setName(name);
                                    place.setLatitude(long_current);
                                    place.setLongitude(lat_current);
                                    System.out.println("VALID PLACE "  + place);
                                    placeList.add(place);
                                }
                            }
                        }
                        if(added == false){
                            JSONObject currentElement = (JSONObject) results.get(0);
                            System.out.println("results " + responseJSON);
                            if (currentElement.length() > 0) {
                                String name = (String) currentElement.get("name");
                                System.out.println("fullFormForCategory " + fullFormForCategory);
                                JSONObject geometry = (JSONObject) currentElement.get("geometry");
                                JSONObject location = (JSONObject) geometry.get("location");
                                Double long_current = (Double) location.get("lng");
                                Double lat_current = (Double) location.get("lat");

                                Place place = new Place();
                                place.setName(name);
                                place.setLatitude(long_current);
                                place.setLongitude(lat_current);
                                System.out.println("VALID PLACE "  + place);
                                placeList.add(place);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
//                }


                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}