package br.wake_in_place.widgets;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import br.wake_in_place.R;
import br.wake_in_place.models.response.AlarmItem;

public class AlarmViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<AlarmItem> alarms;

    public AlarmViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        alarms = AlarmsListWidgetProvider.getAlarms();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return alarms == null ? 0 : alarms.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        AlarmItem alarmItem = alarms.get(position);
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.item_alarms_widget);
        row.setTextViewText(R.id.tv_alarm, String.format(mContext.getString(R.string.label_summary_item), alarmItem.getHour(), alarmItem.getRepeatDays(), alarmItem.getAddress()));
        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
