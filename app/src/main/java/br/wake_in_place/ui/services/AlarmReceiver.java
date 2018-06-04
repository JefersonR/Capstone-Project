package br.wake_in_place.ui.services;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import br.wake_in_place.geofence.GeofenceBroadcastReceiver;
import br.wake_in_place.geofence.GeofenceErrorMessages;
import br.wake_in_place.models.AlarmItem;
import br.wake_in_place.utils.Log;
import br.wake_in_place.utils.ParcelableUtil;

import static br.wake_in_place.ui.activities.RegisterAlarmActivity.ALARM;


/**
 * Created by jeferson on 14/01/16.
 */
public class AlarmReceiver extends BroadcastReceiver implements OnCompleteListener<Void> {
    private GeofencingClient mGeofencingClient;
    private Context context;
    private PendingIntent mGeofencePendingIntent;

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (!task.isSuccessful() && context != null) {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(context, task.getException());
            Log.w( errorMessage);
        }else{
            Log.e( "Tudo certo");
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.e( "Alarme criado");
            this.context = context;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                byte[] bytes  = bundle.getByteArray(ALARM);
                Parcel parcel = ParcelableUtil.unmarshall(bytes);
                AlarmItem alarmItem = new AlarmItem(parcel);
                Log.e("-->" + alarmItem.getAddress());
                if ( alarmItem.isActive() && alarmItem.getLatitude() != 0 && alarmItem.getLongitude() != 0) {
                    Log.e("-->" + alarmItem.getAddress());
                    mGeofencingClient = LocationServices.getGeofencingClient(context);
                    Geofence geofence = new Geofence.Builder()
                            // Set the request ID of the geofence. This is a string to identify this
                            // geofence.
                            .setRequestId(String.valueOf(alarmItem.getId()))

                            .setCircularRegion(
                                    alarmItem.getLatitude(),
                                    alarmItem.getLongitude(),
                                    alarmItem.getRadius()
                            )
                            .setExpirationDuration(alarmItem.getInterval() * 60 * 1000)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                            .build();


                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mGeofencingClient.addGeofences(getGeofencingRequest(geofence), getGeofencePendingIntent(context))
                                .addOnCompleteListener(this);

                }
            }







        }
        catch (Exception e)
        {
            Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }



    }

    private GeofencingRequest getGeofencingRequest( Geofence geofence ) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofence(geofence);

        // Return a GeofencingRequest.
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(Context context) {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

}