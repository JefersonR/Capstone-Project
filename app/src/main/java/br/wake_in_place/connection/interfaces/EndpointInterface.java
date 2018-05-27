package br.wake_in_place.connection.interfaces;

import java.util.List;

import br.wake_in_place.models.response.AlarmItem;
import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by jeferson on 22/11/15.
 */
public interface EndpointInterface {

    /*
    Request method and URL specified in the annotation
    Callback for the parsed response is the last parameter
    */
    //Search movies
    @GET("offer/limit/offset")
    Call<List<AlarmItem>> getAllOffers();

}