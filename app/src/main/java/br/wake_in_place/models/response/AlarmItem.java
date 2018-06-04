package br.wake_in_place.models.response;

import android.os.Parcel;
import android.os.Parcelable;

public class AlarmItem implements Parcelable {

    private int id;
    private String date;
    private String hour;
    private int interval;
    private String repeatDays;
    private String placeID;
    private String address;
    private double latitude;
    private double longitude;
    private boolean isActive;
    private int radius;

    public AlarmItem(){}

    public AlarmItem(int id, String date, String hour, int interval, String repeatDays, String placeID, String address, double latitude, double longitude, boolean isActive, int radius) {
        this.id = id;
        this.date = date;
        this.hour = hour;
        this.interval = interval;
        this.repeatDays = repeatDays;
        this.placeID = placeID;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isActive = isActive;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    protected AlarmItem(Parcel in) {
        id = in.readInt();
        date = in.readString();
        hour = in.readString();
        interval = in.readInt();
        repeatDays = in.readString();
        placeID = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        isActive = in.readByte() != 0x00;
        radius = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(hour);
        dest.writeInt(interval);
        dest.writeString(repeatDays);
        dest.writeString(placeID);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isActive ? 0x01 : 0x00));
        dest.writeInt(radius);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AlarmItem> CREATOR = new Parcelable.Creator<AlarmItem>() {
        @Override
        public AlarmItem createFromParcel(Parcel in) {
            return new AlarmItem(in);
        }

        @Override
        public AlarmItem[] newArray(int size) {
            return new AlarmItem[size];
        }
    };
}