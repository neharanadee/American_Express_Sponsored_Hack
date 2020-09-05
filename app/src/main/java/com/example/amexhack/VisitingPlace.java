package com.example.amexhack;

public class VisitingPlace {

    String placeName = "";
    String imageURL = "";
    Boolean openNow = false;
    String id = "";

    public VisitingPlace(String id, String placeName, String imageURL, Boolean openNow){
        this.placeName = placeName;
        this.imageURL = imageURL;
        this.openNow = openNow;
        this.id = id;

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }


    public Boolean isOpenNow() {
        return openNow;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
