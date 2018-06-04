package br.wake_in_place.connection.interfaces;

import br.wake_in_place.models.response.ClimateResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jeferson on 22/11/15.
 */
public interface EndpointInterface {

    /*
    Request method and URL specified in the annotation
    Callback for the parsed response is the last parameter
    */
    //get climate
    @GET("/data/2.5/weather")
    Call<ClimateResponse> getClimate(@Query("lat") double latitude, @Query("lon") double longitude, @Query("lang") String lang, @Query("units") String units, @Query("APPID") String appid);

}