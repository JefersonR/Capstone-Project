package br.wake_in_place.controllers.detailImpl;

import android.content.Context;
import android.widget.ProgressBar;

import java.util.List;

import br.wake_in_place.connection.BaseImpl;
import br.wake_in_place.connection.GenericRestCallBack;
import br.wake_in_place.connection.interfaces.OnSucess;
import br.wake_in_place.connection.interfaces.RetrofitInterface;
import br.wake_in_place.models.response.AlarmItem;

/**
 * Created by Jeferson on 29/04/2017.
 */

public class DetailImpl extends BaseImpl implements RetrofitInterface {

    private Context context;
    public DetailImpl(Context context) {
        this.context = context;
    }

    public void getAllOffers(OnSucess onSucessListener, ProgressBar progressBar){
        new GenericRestCallBack<List<AlarmItem>>().request(getMyContext(),getApiService().getAllOffers(), onSucessListener, progressBar);
    }

    @Override
    protected Context getMyContext() {
        return context;
    }

    @Override
    protected DetailEndpoint getApiService() {
        return retrofit.create(DetailEndpoint.class);
    }
}
