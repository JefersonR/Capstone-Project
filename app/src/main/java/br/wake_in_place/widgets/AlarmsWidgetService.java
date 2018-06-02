package br.wake_in_place.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class AlarmsWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AlarmViewsFactory(this.getApplicationContext());
    }
}
