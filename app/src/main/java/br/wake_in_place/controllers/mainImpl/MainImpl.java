package br.wake_in_place.controllers.mainImpl;

import android.content.Context;

import br.wake_in_place.BuildConfig;
import br.wake_in_place.connection.BaseImpl;
import br.wake_in_place.connection.GenericRestCallBack;
import br.wake_in_place.connection.interfaces.EndpointInterface;
import br.wake_in_place.connection.interfaces.OnSucess;
import br.wake_in_place.connection.interfaces.RetrofitInterface;
import br.wake_in_place.models.response.ClimateResponse;


/**
 * Created by Jeferson on 29/04/2017.
 */

public class MainImpl extends BaseImpl implements RetrofitInterface {

    private Context context;
    public MainImpl(Context context) {
        this.context = context;
    }
    public void getClimate(double latitude, double longitude, OnSucess onSucessListener){
        final String lang = "pt";
        final String units = "metric";
        new GenericRestCallBack<ClimateResponse>().request(getMyContext(), getApiService().getClimate(latitude,longitude,lang,units,BuildConfig.KEY_WHEATHER),onSucessListener);
    }

    @Override
    protected Context getMyContext() {
        return context;
    }

    @Override
    protected EndpointInterface getApiService() {
        return retrofit.create(EndpointInterface.class);
    }
}
