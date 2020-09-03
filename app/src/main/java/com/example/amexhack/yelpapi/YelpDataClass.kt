package com.example.amexhack.yelpapi

import com.google.gson.annotations.SerializedName

data class YelpSearchResult(
        @SerializedName ("total") val total:Int,
        @SerializedName("businesses")val restaurants: List<YelpRestaurant>
)

data class YelpRestaurant(
        val name: String,
        val id: String,
        val rating: Double,
        val price: String,
        @SerializedName("review_count") val numReviews: Int,
        @SerializedName("distance")val distanceInMeters: Double,
        @SerializedName("image_url") val imageUrl: String,
        val categories: List<YelpCategory>,
        val location: YelpLocation

) {

    fun displayDistance():String{
        val kmPerMeter = 0.001
        val distanceInKm = "%.2f".format(distanceInMeters * kmPerMeter)
        return "$distanceInKm km"
    }
}


data class YelpCategory(
    val title: String
)

data class YelpLocation(
        @SerializedName("address1")val address: String
)

data class YelpReviewsOfPlace(
        @SerializedName("reviews")val reviews: List<YelpReview>
)

data class YelpReview(
        val rating:Int,
        val text:String,
        val user: YelpUser

)

data class YelpUser(
        val name: String
)