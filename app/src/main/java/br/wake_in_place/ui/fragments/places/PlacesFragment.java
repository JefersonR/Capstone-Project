package br.wake_in_place.ui.fragments.places;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.wake_in_place.R;
import br.wake_in_place.controllers.mainImpl.MainImpl;
import br.wake_in_place.models.response.PlaceItem;
import br.wake_in_place.ui.adapters.PlacesAdapter;
import br.wake_in_place.ui.bases.BaseFragment;
import br.wake_in_place.ui.services.AlarmReceiver;
import butterknife.BindView;

import static android.content.Context.ALARM_SERVICE;

public class PlacesFragment extends BaseFragment {


    @BindView(R.id.rc_places)
    RecyclerView rcPlaces;
    @BindView(R.id.txt_nothing)
    TextView txtNothing;
    @BindView(R.id.progress)
    ProgressBar progress;
    public static final String IS_CHOICE = "is_choice";


    private CountDownTimer countDownTimer;

    public static PlacesFragment newInstance() {
        PlacesFragment placesFragment = new PlacesFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_CHOICE, false);
        placesFragment.setArguments(bundle);
        return placesFragment;
    }

    public static PlacesFragment newInstance(boolean isChoice) {
        PlacesFragment placesFragment = new PlacesFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_CHOICE, isChoice);
        placesFragment.setArguments(bundle);
        return placesFragment;
    }

    @Override
    protected void setLayout(View view) {


    }

    @Override
    protected void startProperties() {

      /*  createAlarm(18,8,1);
        createAlarm(18,9,2);
        createAlarm(18,10,3);*/
        List<PlaceItem> items = new ArrayList<>();
        items.add(new PlaceItem("","Avenida Brasil, 123"));
        items.add(new PlaceItem("","Avenida Brasil, 123"));
        items.add(new PlaceItem("","Avenida Brasil, 123"));
        items.add(new PlaceItem("","Avenida Brasil, 123"));
        rcPlaces.setAdapter(new PlacesAdapter(items));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcPlaces.setLayoutManager(llm);
    }



    @Override
    protected void defineListeners() {


    }

    public void createAlarm(int hour_a, int min_a, int broadcast_id) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class); // AlarmReceiver1 = broadcast receiver
        alarmIntent.putExtra("ID", broadcast_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), broadcast_id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, hour_a);
        alarmStartTime.set(Calendar.MINUTE, min_a);
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            Log.d("Hey", "Added a day");
            alarmStartTime.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.e("Alarm", "Alarms set for everyday.");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_places;
    }

    @Override
    protected MainImpl getControllerImpl() {
        return new MainImpl(getMyContext());
    }

}
