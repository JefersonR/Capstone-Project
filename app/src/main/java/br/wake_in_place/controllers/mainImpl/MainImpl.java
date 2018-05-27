package br.wake_in_place.controllers.mainImpl;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import br.wake_in_place.connection.BaseImpl;
import br.wake_in_place.connection.GenericRestCallBack;
import br.wake_in_place.connection.interfaces.OnSucess;
import br.wake_in_place.connection.interfaces.RetrofitInterface;
import br.wake_in_place.models.response.AlarmItem;
import br.wake_in_place.models.response.PlaceItem;

/**
 * Created by Jeferson on 29/04/2017.
 */

public class MainImpl extends BaseImpl implements RetrofitInterface {

    private Context context;
    public MainImpl(Context context) {
        this.context = context;
    }

    public void getAllOffers(OnSucess onSucessListener, View progress, TextView txtNothing){
        new GenericRestCallBack<List<AlarmItem>>().request(getMyContext(), getApiService().getAllOffers(),onSucessListener, progress, txtNothing);
    }

    public void getAllStores(OnSucess onSucessListener, ProgressBar progress, TextView txt){
        new GenericRestCallBack<List<PlaceItem>>().request(getMyContext(), getApiService().getAllStores(),onSucessListener, progress, txt);
    }

    @Override
    protected Context getMyContext() {
        return context;
    }

    @Override
    protected MainEndpoint getApiService() {
        return retrofit.create(MainEndpoint.class);
    }
}
