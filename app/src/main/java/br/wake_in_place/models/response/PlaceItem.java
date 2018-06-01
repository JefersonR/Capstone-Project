package br.wake_in_place.models.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jeferson on 13/07/2017.
 */

public class PlaceItem implements Parcelable {
    private String imgUrl;
    private String address;
    private String id;
    private double latitude;
    private double longitude;

    public PlaceItem(String imgUrl, String address, String id, double latitude, double longitude) {
        this.imgUrl = imgUrl;
        this.address = address;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    protected PlaceItem(Parcel in) {
        imgUrl = in.readString();
        address = in.readString();
        id = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(address);
        dest.writeString(id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PlaceItem> CREATOR = new Parcelable.Creator<PlaceItem>() {
        @Override
        public PlaceItem createFromParcel(Parcel in) {
            return new PlaceItem(in);
        }

        @Override
        public PlaceItem[] newArray(int size) {
            return new PlaceItem[size];
        }
    };
}