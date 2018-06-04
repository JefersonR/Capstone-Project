package br.wake_in_place.ui.fragments.alarm;

import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.wake_in_place.R;
import br.wake_in_place.controllers.mainImpl.MainImpl;
import br.wake_in_place.data.WakePlaceDBContract;
import br.wake_in_place.models.response.AlarmItem;
import br.wake_in_place.ui.adapters.AlarmAdapter;
import br.wake_in_place.ui.bases.BaseFragment;
import butterknife.BindView;

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
                        cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.PLACE_ID)),
                        cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.ADDRESS)),
                        cursor.getDouble(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.LONGITUDE)),
                        cursor.getInt(cursor.getColumnIndex(WakePlaceDBContract.AlarmsBD.Cols.ACTIVE)) > 0,
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
    public void onStop() {
        super.onStop();
        if (cursor != null) cursor.close();
    }



}
