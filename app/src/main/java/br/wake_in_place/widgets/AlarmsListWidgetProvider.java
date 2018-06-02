package br.wake_in_place.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.List;

import br.wake_in_place.R;
import br.wake_in_place.models.response.AlarmItem;

/**
 * Implementation of App Widget functionality.
 */
public class AlarmsListWidgetProvider extends AppWidgetProvider {

    public static final String NEXT_RECIPE = "NEXT_RECIPE";

    private static List<AlarmItem> alarmList;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        if (alarmList != null && !alarmList.isEmpty()) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.alarms_list_widget);
                // Set's up the listview
                Intent adapterIntent = new Intent(context, AlarmsWidgetService.class);
                views.setRemoteAdapter(R.id.lv_alarms, adapterIntent);

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }


    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        AlarmsIntentService.startActionFetchRecipes(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static List<AlarmItem> getAlarms() {
        return alarmList;
    }

    public static void setAlarmList(List<AlarmItem> list) {
        alarmList = list;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetsId = appWidgetManager.getAppWidgetIds(new ComponentName(context, AlarmsListWidgetProvider.class));
            updateAppWidgets(context, appWidgetManager, appWidgetsId);
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, AlarmsListWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.lv_alarms);

        super.onReceive(context, intent);
    }
}

