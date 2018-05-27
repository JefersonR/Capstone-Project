package br.wake_in_place.ui.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
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
import br.wake_in_place.ui.bases.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class RegisterAlarmActivity extends BaseActivity {

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
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    @Override
    protected void setLayout(View view) {

    }

    @Override
    protected void startProperties() {
        setToolbar(getString(R.string.title_new_alarm));
        btnDistance.setText(String.format(getString(R.string.label_distance_edit), "500mts"));
    }

    @Override
    protected void defineListeners() {

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
         startActivity(new Intent(this,MyPlacesActivity.class));
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
        setHour(tvHourEnd);
    }


    @OnClick(R.id.btn_distance)
    public void setbtnDistance(View view) {
        numberPickershow();
    }

    @OnClick(R.id.btn_mark_all)
    public void setBtnMarkAll(View view) {
        checkRepeat.setChecked(true);
        toggleButton.setChecked(true);
        toggleButton2.setChecked(true);
        toggleButton3.setChecked(true);
        toggleButton4.setChecked(true);
        toggleButton5.setChecked(true);
        toggleButton6.setChecked(true);
        toggleButton7.setChecked(true);
    }

    @OnClick(R.id.btn_mark_off)
    public void setBtnUnmarkAll(View view) {
        checkRepeat.setChecked(false);
        toggleButton.setChecked(false);
        toggleButton2.setChecked(false);
        toggleButton3.setChecked(false);
        toggleButton4.setChecked(false);
        toggleButton5.setChecked(false);
        toggleButton6.setChecked(false);
        toggleButton7.setChecked(false);
    }

    public void numberPickershow() {
        final Dialog dialog = new Dialog(this, R.style.DateDialog);
        dialog.setTitle(R.string.title_distance);
        dialog.setContentView(R.layout.number_picker_dialog);
        Button aply = (Button) dialog.findViewById(R.id.apply_button);

        final String[] values = getResources().getStringArray(R.array.distances);

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
                btnDistance.setText(String.format(getString(R.string.label_distance_edit), values[np.getValue()]));
                dialog.dismiss();
            }
        });

        dialog.show();
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
                textView.setText(selected);
                textView.setTextColor(ContextCompat.getColor(RegisterAlarmActivity.this, R.color.colorAccent));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void setDateTimeField(final TextView textView) {

        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(this, R.style.DateDialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                newDate.set(year, monthOfYear, dayOfMonth);
                String selected = dateFormatter2.format(newDate.getTime());
                textView.setText(dateFormatter.format(newDate.getTime()));
                textView.setTextColor(ContextCompat.getColor(RegisterAlarmActivity.this, R.color.colorAccent));

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
}
