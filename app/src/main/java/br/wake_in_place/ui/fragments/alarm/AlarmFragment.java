package br.wake_in_place.ui.fragments.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.wake_in_place.R;
import br.wake_in_place.controllers.mainImpl.MainImpl;
import br.wake_in_place.models.response.AlarmItem;
import br.wake_in_place.ui.adapters.AlarmAdapter;
import br.wake_in_place.ui.bases.BaseFragment;
import br.wake_in_place.ui.services.AlarmReceiver;
import br.wake_in_place.utils.Log;
import butterknife.BindView;

import static android.content.Context.ALARM_SERVICE;

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
    protected void startProperties() {
        List<AlarmItem> items = new ArrayList<>();
        rcAlarms.setAdapter(new AlarmAdapter(items, this));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcAlarms.setLayoutManager(llm);

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
            android.util.Log.d("Hey", "Added a day");
            alarmStartTime.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        android.util.Log.e("Alarm", "Alarms set for everyday.");
    }
}
