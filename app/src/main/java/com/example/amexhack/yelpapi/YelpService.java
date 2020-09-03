package com.example.amexhack.yelpapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface YelpService {

    @GET("search")
    public Call<YelpSearchResult> searchRestaurants(@Header ("Authorization") String authHeader, @Query("term")String searchTerm, @Query("location") String location);


    @GET("reviews")
    public Call<YelpReviewsOfPlace> searchReviews(@Header("Authorization") String authHeader);
}
