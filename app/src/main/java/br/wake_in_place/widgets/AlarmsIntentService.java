package br.wake_in_place.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.wake_in_place.data.WakePlaceDBContract;
import br.wake_in_place.models.AlarmItem;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class AlarmsIntentService extends IntentService {

    public static final String ALARMS_LIST = "br.wake_in_place.action.ALARMS_LIST";

    public static void startActionFetchRecipes(Context context) {
        Intent intent = new Intent(context, AlarmsIntentService.class);
        intent.setAction(ALARMS_LIST);
        context.startService(intent);
    }

    public AlarmsIntentService() {
        super("AlarmsIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ALARMS_LIST.equals(action)) {
                getAlarms();
            }
        }
    }

    private void getAlarms() {
        List<AlarmItem> items = loadContent();
        if (items != null) {
            AlarmsListWidgetProvider.setAlarmList(items);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
            int[] appWidgetsId = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), AlarmsListWidgetProvider.class));
            AlarmsListWidgetProvider.updateAppWidgets(getApplicationContext(), appWidgetManager, appWidgetsId);

        }


    }

    private List<AlarmItem> loadContent() {
        Cursor cursor = this.getContentResolver().query(WakePlaceDBContract.AlarmsBD.CONTENT_URI, null, null, null, null);
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
}
