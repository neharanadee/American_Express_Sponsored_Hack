package com.example.amexhack.yelpapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface YelpService {



    @GET("search")
    public Call<YelpSearchResult> searchRestaurants(@Header ("Authorization") String authHeader, @Query("term")String searchTerm, @Query("latitude") Double latitude, @Query("longitude") Double longitude);


    @GET("search")
    public Call<YelpSearchResult> searchRestaurants(@Header ("Authorization") String authHeader, @Query("term")String searchTerm, @Query("location") String location);

    //latitude	decimal	Required if location is not provided. Latitude of the location you want to search nearby.
    //longitude	decimal
    @GET("reviews")
    public Call<YelpReviewsOfPlace> searchReviews(@Header("Authorization") String authHeader);

    @GET("{id}")
    public Call<YelpBusiness> searchBusinessDetails(@Path(value = "id", encoded = true)String id, @Header("Authorization") String authHeader);


}
