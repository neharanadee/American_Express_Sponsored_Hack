package com.example.amexhack;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.amexhack.MainActivity.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import com.example.amexhack.MapActivity.TaskDirectionRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import java.net.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.json.simple.parser.JSONParser;
import java.io.InputStream;

class OptimalDistance  {


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



    static private String api_key = "AIzaSyATPwWeg_lhIP1Lh3Mls3U_WSaY3AI21G4";

    HashMap<String, HashSet<String>> allCategoriesWithPlaces = new HashMap<String, HashSet<String>>();

    public OptimalDistance() throws IOException, ParseException {
        try {

//            FileReader stream = (new FileReader("app/src/main/java/com/example/amexhack/Categories.json"));
//            int b;
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((b = stream.read()) != -1) {
//                stringBuilder.append((char) b);
//            }
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








    public ArrayList<Place> findAllPlaces(ArrayList<String> categories, double latitude, double longitude) throws JSONException {

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






}