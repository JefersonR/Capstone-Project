
package br.wake_in_place.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ClimateResponse implements Parcelable
{
    private List<Weather> weather = new ArrayList<Weather>();
    private String base;
    private Main main;
    private int visibility;
    private int dt;
    private int id;
    private String name;
    private int cod;
    public final static Creator<ClimateResponse> CREATOR = new Creator<ClimateResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ClimateResponse createFromParcel(Parcel in) {
            return new ClimateResponse(in);
        }

        public ClimateResponse[] newArray(int size) {
            return (new ClimateResponse[size]);
        }

    }
    ;

    protected ClimateResponse(Parcel in) {

        in.readList(this.weather, (br.wake_in_place.models.response.Weather.class.getClassLoader()));
        this.base = ((String) in.readValue((String.class.getClassLoader())));
        this.main = ((Main) in.readValue((Main.class.getClassLoader())));
        this.visibility = ((int) in.readValue((int.class.getClassLoader())));
        this.dt = ((int) in.readValue((int.class.getClassLoader())));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.cod = ((int) in.readValue((int.class.getClassLoader())));
    }

    public ClimateResponse() {
    }



    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(weather);
        dest.writeValue(base);
        dest.writeValue(main);
        dest.writeValue(visibility);
        dest.writeValue(dt);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(cod);
    }

    public int describeContents() {
        return  0;
    }

}
