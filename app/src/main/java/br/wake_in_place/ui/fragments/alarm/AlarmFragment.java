package br.wake_in_place.ui.fragments.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
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
import br.wake_in_place.data.WakePlaceDBContract;
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
    private Cursor cursor;

    public static AlarmFragment newInstance() {
        return  new AlarmFragment();
    }


    @Override
    protected void startProperties() {
        createAlarm(15,16,1);

    }

    @Override
    public void onResume() {
        super.onResume();
        List<AlarmItem> items = loadContent();
        txtNothing.setVisibility((items == null || items.isEmpty()) ? View.VISIBLE : View.GONE);
        rcAlarms.setAdapter(new AlarmAdapter(items, this));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcAlarms.setLayoutManager(llm);
    }

    private List<AlarmItem> loadContent() {
        cursor = getActivity().getContentResolver().query(WakePlaceDBContract.AlarmsBD.CONTENT_URI, null, null, null, null);
        List<AlarmItem> items = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                AlarmItem alarmItem = new AlarmItem(
                        cursor.getInt(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.ID)),
                        cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.DATE)),
                        cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.HOUR)),
                        cursor.getInt(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.INTERVAL)),
                        cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.REPEAT_DAYS)),
                        cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.PLACE_ID)),
                        cursor.getInt(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.RADIUS))

                );
                items.add(alarmItem);
            } while (cursor.moveToNext());
        }
        return items;
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

    }

    @Override
    public void onCheckListener(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onStop() {
        super.onStop();
        if (cursor != null) cursor.close();
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
//            alarmStartTime.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
