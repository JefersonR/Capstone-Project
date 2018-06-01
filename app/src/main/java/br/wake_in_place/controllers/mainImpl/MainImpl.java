package br.wake_in_place.controllers.mainImpl;

import android.content.Context;

import br.wake_in_place.connection.BaseImpl;
import br.wake_in_place.connection.interfaces.EndpointInterface;
import br.wake_in_place.connection.interfaces.RetrofitInterface;


/**
 * Created by Jeferson on 29/04/2017.
 */

public class MainImpl extends BaseImpl implements RetrofitInterface {

    private Context context;
    public MainImpl(Context context) {
        this.context = context;
    }

/*    public void getStaticMap(double latitude, double longitude, OnSucess onSucessListener){
        final String size = "400x400";
        final String key = BuildConfig.KEY;;
        final int zoom = 14;

        new GenericRestCallBack<String>().request(getMyContext(), getApiService().getStaticMap(latitude+","+longitude,zoom,size,key),onSucessListener, true);
    }*/

    @Override
    protected Context getMyContext() {
        return context;
    }

    @Override
    protected EndpointInterface getApiService() {
        return retrofit.create(EndpointInterface.class);
    }
}
