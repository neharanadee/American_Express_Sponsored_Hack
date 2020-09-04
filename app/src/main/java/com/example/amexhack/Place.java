package com.example.amexhack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

public class Place{
    String name = "";
    double latitude = 0;
    double longitude = 0;

    public HashMap<String, HashSet<String>> allCategoriesWithPlaces = new HashMap<String, HashSet<String>>();

    public Place(){

    }

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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }


}