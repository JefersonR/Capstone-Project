package br.wake_in_place.connection.interfaces;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.wake_in_place.BuildConfig;
import br.wake_in_place.connection.NetworkSetup;
import br.wake_in_place.utils.ArrayAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by jeferson on 22/11/15.
 */
public interface RetrofitInterface {

     final String dateFormat = "yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'";
    //Define uma interface para conexão com o servidro.
    final String BASE_URL = BuildConfig.API_HOST;
    Gson gson = new GsonBuilder()
            .setDateFormat(dateFormat)
            .registerTypeAdapterFactory(new ArrayAdapterFactory())
            .create();


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(NetworkSetup.getClient())
            .build();



}
