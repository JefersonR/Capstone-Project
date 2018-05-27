package br.wake_in_place.models.response;

/**
 * Created by Jeferson on 13/07/2017.
 */

public class PlaceItem {
    private String imgUrl;
    private String Place;

    public PlaceItem(String imgUrl, String place) {
        this.imgUrl = imgUrl;
        Place = place;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }
}