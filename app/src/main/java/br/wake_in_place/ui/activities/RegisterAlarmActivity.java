package br.wake_in_place.ui.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.wake_in_place.R;
import br.wake_in_place.controllers.detailImpl.DetailImpl;
import br.wake_in_place.data.WakePlaceDBContract;
import br.wake_in_place.models.AlarmItem;
import br.wake_in_place.models.PlaceItem;
import br.wake_in_place.ui.bases.BaseActivity;
import br.wake_in_place.ui.services.AlarmReceiver;
import br.wake_in_place.utils.Constants;
import br.wake_in_place.utils.DialogCustomUtil;
import br.wake_in_place.utils.Log;
import br.wake_in_place.utils.ParcelableUtil;
import butterknife.BindView;
import butterknife.OnClick;

import static br.wake_in_place.ui.fragments.places.PlacesFragment.IS_CHOICE;

public class RegisterAlarmActivity extends BaseActivity implements Constants {

    @BindView(R.id.tv_date_start)
    TextView tvDateStart;
    @BindView(R.id.tv_date_end)
    TextView tvDateEnd;
    @BindView(R.id.tv_hour_start)
    TextView tvHourStart;
    @BindView(R.id.tv_hour_end)
    TextView tvHourEnd;
    @BindView(R.id.check_repeat)
    AppCompatCheckBox checkRepeat;
    @BindView(R.id.btn_mark_all)
    Button btnMarkAll;
    @BindView(R.id.btn_mark_off)
    Button btnMarkOff;
    @BindView(R.id.relative_days)
    LinearLayout relativeDays;
    @BindView(R.id.toggleButton)
    ToggleButton toggleButton;
    @BindView(R.id.toggleButton2)
    ToggleButton toggleButton2;
    @BindView(R.id.toggleButton3)
    ToggleButton toggleButton3;
    @BindView(R.id.toggleButton4)
    ToggleButton toggleButton4;
    @BindView(R.id.toggleButton5)
    ToggleButton toggleButton5;
    @BindView(R.id.toggleButton6)
    ToggleButton toggleButton6;
    @BindView(R.id.toggleButton7)
    ToggleButton toggleButton7;
    @BindView(R.id.btn_my_places)
    Button btnMyPlaces;
    @BindView(R.id.btn_places)
    Button btnPlaces;
    @BindView(R.id.btn_distance)
    Button btnDistance;
    @BindView(R.id.tv_summary)
    TextView tvSummary;
    @BindView(R.id.btn_save)
    Button btnSave;
    private int PLACE_PICKER_REQUEST = 1;
    private int MY_PLACES_REQUEST = 2;
    public static String ALARM = "ALARM";
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    private AlarmItem alarmItem;
    private ContentResolver contentResolver;

    @Override
    protected void startProperties() {
        setToolbar(getString(R.string.title_new_alarm));
        btnDistance.setText(String.format(getString(R.string.label_distance_edit), getString(R.string.str_init_distance)));
        alarmItem = new AlarmItem();
        contentResolver = this.getContentResolver();
        alarmItem.setRadius(getRadius(0));
        alarmItem.setInterval(getInterval(1));
        alarmItem.setActive(true);
        defineDays();
        summary();
        checkRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                       if (!buttonView.isChecked()) {
                                                           setTogglesChecked(false);
                                                       }
                                                       summary();
                                                   }
                                               }
        );
        togglesListener();
    }

    private void togglesListener() {
        for (ToggleButton toggles : getToggles()) {
            toggles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    defineDays();
                    summary();
                }
            });
        }
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_register_alarm;
    }

    @Override
    protected Context getMyContext() {
        return RegisterAlarmActivity.this;
    }

    @Override
    protected DetailImpl getControllerImpl() {
        return new DetailImpl(getMyContext());
    }

    @OnClick(R.id.btn_places)
    public void setBtnPlaces(View view) {
        try {
            startActivityForResult(builder.build(RegisterAlarmActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_my_places)
    public void setBtnMyPlaces(View view) {
        startActivityForResult(new Intent(this, MyPlacesActivity.class), MY_PLACES_REQUEST);
    }

    @OnClick(R.id.tv_date_start)
    public void setTvDateStart(View view) {
        setDateTimeField(tvDateStart);
    }

    @OnClick(R.id.tv_hour_start)
    public void setTvHourStart(View view) {
        setHour(tvHourStart);
    }

    @OnClick(R.id.tv_hour_end)
    public void setTvHourEnd(View view) {
        numberPickershow(false, getResources().getStringArray(R.array.times));
    }

    @OnClick(R.id.btn_save)
    public void setBtnSave(View view) {
        if (verifyFields()) {
            saveAlarm(getNewAlarm());
        } else {
            DialogCustomUtil.dialog(this, getString(R.string.title_attention), getString(R.string.label_error_dialog_alarm));
        }

    }

    private void summary() {
        tvSummary.setText(String.format(getString(R.string.label_summary), alarmItem.getDate() != null ? alarmItem.getDate() : " - ",
                alarmItem.getHour() != null ? alarmItem.getHour() : " - ", String.valueOf(alarmItem.getInterval()),
                alarmItem.getRepeatDays() != null && !alarmItem.getRepeatDays().isEmpty() ? alarmItem.getRepeatDays() : getString(R.string.label_no_location),
                alarmItem.getAddress() != null && !alarmItem.getAddress().isEmpty() ? alarmItem.getAddress() : getString(R.string.label_no_location_summary),
                String.valueOf(alarmItem.getRadius())
        ));
    }

    private boolean verifyFields() {
        boolean verify = true;
        if (alarmItem.getDate() == null || alarmItem.getDate().isEmpty()) {
            verify = false;
        }
        if (alarmItem.getHour() == null || alarmItem.getHour().isEmpty()) {
            verify = false;
        }
        return verify;
    }

    private void saveAlarm(AlarmItem alarmItem) {

        ContentValues val = new ContentValues();
        val.put(WakePlaceDBContract.AlarmsBD.Cols.DATE, alarmItem.getDate());
        val.put(WakePlaceDBContract.AlarmsBD.Cols.HOUR, alarmItem.getHour());
        val.put(WakePlaceDBContract.AlarmsBD.Cols.INTERVAL, alarmItem.getInterval());
        val.put(WakePlaceDBContract.AlarmsBD.Cols.REPEAT_DAYS, alarmItem.getRepeatDays());
        val.put(WakePlaceDBContract.AlarmsBD.Cols.PLACE_ID, alarmItem.getPlaceID());
        val.put(WakePlaceDBContract.AlarmsBD.Cols.ADDRESS, alarmItem.getAddress());
        val.put(WakePlaceDBContract.AlarmsBD.Cols.RADIUS, alarmItem.getRadius());
        val.put(WakePlaceDBContract.AlarmsBD.Cols.LATITUDE, alarmItem.getLatitude());
        val.put(WakePlaceDBContract.AlarmsBD.Cols.LONGITUDE, alarmItem.getLongitude());
        val.put(WakePlaceDBContract.AlarmsBD.Cols.ACTIVE, 1);
        Uri uri = contentResolver.insert(WakePlaceDBContract.AlarmsBD.CONTENT_URI, val);
        long rowId = Long.valueOf(uri.getLastPathSegment());
        alarmItem.setId((int) rowId);
        createAlarms(alarmItem);
        finish();
    }

    private void createAlarms(AlarmItem alarmItem) {
        String days[] = alarmItem.getRepeatDays().split(" ");
        if (days.length != 0) {
            for (String day : days) {
                switch (day) {
                    case SEG:
                        createAlarm(alarmItem, Calendar.MONDAY);
                        break;
                    case TER:
                        createAlarm(alarmItem, Calendar.TUESDAY);
                        break;
                    case QUA:
                        createAlarm(alarmItem, Calendar.WEDNESDAY);
                        break;
                    case QUI:
                        createAlarm(alarmItem, Calendar.THURSDAY);
                        break;
                    case SEX:
                        createAlarm(alarmItem, Calendar.FRIDAY);
                        break;
                    case SAB:
                        createAlarm(alarmItem, Calendar.SATURDAY);
                        break;
                    case DOM:
                        createAlarm(alarmItem, Calendar.SUNDAY);
                        break;
                }
            }
        } else {
            createAlarm(alarmItem, -1);
        }
    }




    public void createAlarm(AlarmItem alarmItem, int dayOfWeek) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(RegisterAlarmActivity.this, AlarmReceiver.class); // AlarmReceiver1 = broadcast receiver
        alarmIntent.putExtra(ALARM, ParcelableUtil.marshall(alarmItem));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(RegisterAlarmActivity.this, alarmItem.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT );
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar alarmStartTime = Calendar.getInstance();

        alarmStartTime.set(Calendar.DAY_OF_MONTH, Integer.valueOf(alarmItem.getDate().split("/")[0]) );
        alarmStartTime.set(Calendar.MONTH, Integer.valueOf(alarmItem.getDate().split("/")[1]) - 1);
        alarmStartTime.set(Calendar.YEAR,Integer.valueOf(alarmItem.getDate().split("/")[2]) );
        alarmStartTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(alarmItem.getHour().split(":")[0]));
        alarmStartTime.set(Calendar.MINUTE, Integer.valueOf(alarmItem.getHour().split(":")[1]));
        alarmStartTime.set(Calendar.SECOND, 0);

        if (dayOfWeek != -1) {
            alarmStartTime.set(Calendar.DAY_OF_WEEK, dayOfWeek);
            alarmStartTime.add(Calendar.DATE, -7);
            Log.e(alarmStartTime.getTime().toString());
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP,alarmStartTime.getTimeInMillis(),pendingIntent);
        }
    }


    @OnClick(R.id.btn_distance)
    public void setbtnDistance(View view) {
        numberPickershow(true, getResources().getStringArray(R.array.distances));
    }

    @OnClick(R.id.btn_mark_all)
    public void setBtnMarkAll(View view) {
        checkRepeat.setChecked(true);
        setTogglesChecked(true);
    }

    @OnClick(R.id.btn_mark_off)
    public void setBtnUnmarkAll(View view) {
        checkRepeat.setChecked(false);
        setTogglesChecked(false);
    }

    private ToggleButton[] getToggles() {
        return new ToggleButton[]{toggleButton, toggleButton2, toggleButton3, toggleButton4, toggleButton5, toggleButton6, toggleButton7};
    }

    private void setTogglesChecked(boolean isChecked) {
        for (ToggleButton toggles : getToggles()) {
            toggles.setChecked(isChecked);
        }
    }

    public void numberPickershow(final boolean isDistance, final String[] values) {
        final Dialog dialog = new Dialog(this, R.style.DateDialog);
        dialog.setContentView(R.layout.number_picker_dialog);
        Button aply = (Button) dialog.findViewById(R.id.apply_button);
        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.number_picker);
        np.setMaxValue(values.length - 1);
        np.setMinValue(0);
        np.setDisplayedValues(values);
        np.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        try {
            np.setWrapSelectorWheel(true);
        } catch (Exception e) {
            e.getMessage();
        }

        aply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDistance) {
                    alarmItem.setRadius(getRadius(np.getValue()));
                    btnDistance.setText(String.format(getString(R.string.label_distance_edit), values[np.getValue()]));
                } else {
                    alarmItem.setInterval(getInterval(np.getValue()));
                    tvHourEnd.setText(values[np.getValue()]);
                }
                dialog.dismiss();
                summary();
            }
        });

        dialog.show();
    }

    private int getInterval(int value) {
        int interval = 60;
        switch (value) {
            case 0:
                interval = 30;
                break;
            case 1:
                interval = 60;
                break;
            case 2:
                interval = 90;
                break;
            case 3:
                interval = 120;
                break;
            case 4:
                interval = 180;
                break;
            case 5:
                interval = 300;
                break;
        }
        summary();
        return interval;
    }

    private int getRadius(int value) {
        int distance = 500;
        switch (value) {
            case 0:
                distance = 500;
                break;
            case 1:
                distance = 1000;
                break;
            case 2:
                distance = 1500;
                break;
            case 3:
                distance = 2000;
                break;
            case 4:
                distance = 3000;
                break;
            case 5:
                distance = 5000;
                break;
        }
        summary();
        return distance;
    }

    private void setHour(final TextView textView) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, R.style.DateDialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String selected;
                if (selectedMinute < 10) {
                    selected = selectedHour + ":0" + selectedMinute;
                } else {
                    selected = selectedHour + ":" + selectedMinute;
                }
                alarmItem.setHour(selected);
                textView.setText(selected);
                textView.setTextColor(ContextCompat.getColor(RegisterAlarmActivity.this, R.color.colorAccent));
                summary();
            }
        }, hour, minute, true);//Yes 24 hour time

        mTimePicker.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            alarmItem.setAddress((String) place.getAddress());
            alarmItem.setPlaceID((String) place.getId());
            alarmItem.setLatitude(place.getLatLng().latitude);
            alarmItem.setLongitude(place.getLatLng().longitude);
        } else if (requestCode == MY_PLACES_REQUEST && resultCode == RESULT_OK) {
            PlaceItem myPlace = data.getParcelableExtra(IS_CHOICE);
            alarmItem.setAddress(myPlace.getAddress());
            alarmItem.setPlaceID(myPlace.getId());
            alarmItem.setLatitude(myPlace.getLatitude());
            alarmItem.setLongitude(myPlace.getLongitude());
        }
        summary();
    }

    protected void setDateTimeField(final TextView textView) {

        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(this, R.style.DateDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                newDate.set(year, monthOfYear, dayOfMonth);
                textView.setText(dateFormatter.format(newDate.getTime()));
                textView.setTextColor(ContextCompat.getColor(RegisterAlarmActivity.this, R.color.colorAccent));
                alarmItem.setDate(dateFormatter.format(newDate.getTime()));
                summary();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            fromDatePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        } catch (Exception e) {
            e.getMessage();
        }

        fromDatePickerDialog.show();
    }

    public AlarmItem getNewAlarm() {
        return alarmItem;
    }


    private void defineDays() {
        StringBuilder text = new StringBuilder();
        text.setLength(0);

        if (toggleButton.isChecked()) {
            text.append(SEG + " ");
        }
        if (toggleButton2.isChecked()) {
            text.append(TER + " ");
        }
        if (toggleButton3.isChecked()) {
            text.append(QUA + " ");
        }
        if (toggleButton4.isChecked()) {
            text.append(QUI + " ");
        }
        if (toggleButton5.isChecked()) {
            text.append(SEX + " ");
        }
        if (toggleButton6.isChecked()) {
            text.append(SAB + " ");
        }
        if (toggleButton7.isChecked()) {
            text.append(DOM);
        }
        text.setLength(text.length());
        alarmItem.setRepeatDays(text.toString());
        summary();
    }
}
