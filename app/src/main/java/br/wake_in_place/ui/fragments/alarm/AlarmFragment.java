package br.wake_in_place.ui.fragments.alarm;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.wake_in_place.R;
import br.wake_in_place.controllers.mainImpl.MainImpl;
import br.wake_in_place.models.response.AlarmItem;
import br.wake_in_place.ui.adapters.AlarmAdapter;
import br.wake_in_place.ui.bases.BaseFragment;
import br.wake_in_place.utils.Log;
import butterknife.BindView;

public class AlarmFragment extends BaseFragment implements AlarmAdapter.AlarmListener{

    @BindView(R.id.rc_alarms)
    RecyclerView rcAlarms;
    @BindView(R.id.txt_nothing)
    TextView txtNothing;
    @BindView(R.id.progress)
    ProgressBar progress;

    public static AlarmFragment newInstance() {
        return  new AlarmFragment();
    }

    @Override
    protected void setLayout(View view) {

    }

    @Override
    protected void startProperties() {
        List<AlarmItem> items = new ArrayList<>();
        items.add(new AlarmItem("17:15","S T Q Q S S","Avenida Brasil, 123"));
        items.add(new AlarmItem("18:15","S T Q Q S S","Avenida Brasil, 123"));
        items.add(new AlarmItem("19:15","S T Q Q S S","Avenida Brasil, 123"));
        items.add(new AlarmItem("20:15","S T Q Q S S","Avenida Brasil, 123"));
        rcAlarms.setAdapter(new AlarmAdapter(items, this));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcAlarms.setLayoutManager(llm);

    }


    @Override
    protected void defineListeners() {


    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_alarm;
    }

    @Override
    protected MainImpl getControllerImpl() {
        return new MainImpl(getMyContext());
    }


    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onLongItemClick(View view) {
        Log.e("LONGGGG");
    }

    @Override
    public void onCheckListener(CompoundButton buttonView, boolean isChecked) {

    }
}
