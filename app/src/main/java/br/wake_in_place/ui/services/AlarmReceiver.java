package br.wake_in_place.ui.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import br.wake_in_place.utils.Log;


/**
 * Created by jeferson on 14/01/16.
 */
public class AlarmReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            Bundle bundle = intent.getExtras();
            int message = bundle.getInt("ID");
            Log.e( message +"");

            Intent service1 = new Intent(context, NotificationService.class);
            service1.putExtra("ID", message);
            service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
            context.startService(service1);

        }
        catch (Exception e)
        {
            Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }



    }

}