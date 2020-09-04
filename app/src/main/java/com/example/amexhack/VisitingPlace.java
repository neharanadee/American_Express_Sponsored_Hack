package com.example.amexhack;

public class VisitingPlace {

    String placeName = "";
    String imageURL = "";
    Boolean openNow = false;

    public VisitingPlace(String placeName, String imageURL, Boolean openNow){
        this.placeName = placeName;
        this.imageURL = imageURL;
        this.openNow = openNow;

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


}
