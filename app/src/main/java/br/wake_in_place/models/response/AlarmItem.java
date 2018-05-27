package br.wake_in_place.models.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jeferson on 22/03/2016.
 */
public class AlarmItem implements Parcelable
{

    private String hour;
    private String days;
    private String address;
    public final static Parcelable.Creator<AlarmItem> CREATOR = new Creator<AlarmItem>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AlarmItem createFromParcel(Parcel in) {
            return new AlarmItem(in);
        }

        public AlarmItem[] newArray(int size) {
            return (new AlarmItem[size]);
        }

    } ;

    public AlarmItem(String hour, String days, String address) {
        this.hour = hour;
        this.days = days;
        this.address = address;
    }

    protected AlarmItem(Parcel in) {
        this.hour = ((String) in.readValue((String.class.getClassLoader())));
        this.days = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AlarmItem() {
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(hour);
        dest.writeValue(days);
        dest.writeValue(address);
    }

    public int describeContents() {
        return 0;
    }

}
