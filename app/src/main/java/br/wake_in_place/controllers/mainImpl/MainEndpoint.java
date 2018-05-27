package br.wake_in_place.controllers.mainImpl;

import java.util.List;

import br.wake_in_place.models.response.AlarmItem;
import br.wake_in_place.models.response.PlaceItem;
import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by jeferson on 22/11/15.
 */
public interface MainEndpoint {

    /*
    Request method and URL specified in the annotation
    Callback for the parsed response is the last parameter
    */
    //Search Offers
    @GET("offer/limit/offset")
    Call<List<AlarmItem>> getAllOffers();

    //Search Offers
    @GET("store/limit/offset")
    Call<List<PlaceItem>> getAllStores();

}