package br.wake_in_place.controllers.detailImpl;

import android.content.Context;

import br.wake_in_place.connection.BaseImpl;
import br.wake_in_place.connection.interfaces.EndpointInterface;
import br.wake_in_place.connection.interfaces.RetrofitInterface;

/**
 * Created by Jeferson on 29/04/2017.
 */

public class DetailImpl extends BaseImpl implements RetrofitInterface {

    private Context context;
    public DetailImpl(Context context) {
        this.context = context;
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
