package br.wake_in_place.ui.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;

import br.wake_in_place.R;
import br.wake_in_place.connection.interfaces.OnSucess;
import br.wake_in_place.controllers.mainImpl.MainImpl;
import br.wake_in_place.data.WakePlaceContentProvider;
import br.wake_in_place.data.WakePlaceDBContract;
import br.wake_in_place.models.response.ClimateResponse;
import br.wake_in_place.ui.bases.BaseActivity;
import br.wake_in_place.ui.fragments.alarm.AlarmFragment;
import br.wake_in_place.ui.fragments.places.PlacesFragment;
import br.wake_in_place.utils.DialogCustomUtil;
import br.wake_in_place.utils.Log;
import br.wake_in_place.utils.Snackbar;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    int PLACE_PICKER_REQUEST = 1;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    private AlarmFragment alarmFragment;
    private PlacesFragment placesFragment;

    private ContentResolver contentResolver;
    private FusedLocationProviderClient mFusedLocationClient;

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private boolean isAlarmPage = true;


    @Override
    protected void startProperties() {
        setToolbar(R.id.toolbar, false, getString(R.string.app_name));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        contentResolver = this.getContentResolver();
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                isAlarmPage = position == 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        location();


    }

    private void location() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            getControllerImpl().getClimate(location.getLatitude(), location.getLongitude(), new OnSucess() {
                                @Override
                                public void onSucessResponse(Response response) {
                                    ClimateResponse climate = (ClimateResponse) response.body();
                                    if (alarmFragment == null) {
                                        alarmFragment = AlarmFragment.newInstance();
                                    }
                                    if (climate != null)
                                        alarmFragment.updateClimate(String.format(getString(R.string.label_cimate),
                                                climate.getName(),
                                                climate.getWeather() != null && !climate.getWeather().isEmpty() ? ", " + climate.getWeather().get(0).getDescription() : "",
                                                climate.getMain().getTemp()));
                                }
                            });
                        }
                    }
                });
    }

    @OnClick(R.id.fab)
    public void setBtnFab(View view) {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            if (isAlarmPage) {
                startActivity(new Intent(MainActivity.this, RegisterAlarmActivity.class));
            } else {
                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected Context getMyContext() {
        return MainActivity.this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (alarmFragment == null) {
                        alarmFragment = AlarmFragment.newInstance();
                    }
                    return alarmFragment;
                case 1:
                    if (placesFragment == null) {
                        placesFragment = PlacesFragment.newInstance();
                    }
                    return placesFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_alarm_tab);
                case 1:
                    return getString(R.string.title_places_tab);
            }
            return null;
        }
    }

    @Override
    protected MainImpl getControllerImpl() {
        return new MainImpl(getMyContext());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                WakePlaceContentProvider provider = new WakePlaceContentProvider();
                if (!provider.containPlace(place.getId(), MainActivity.this)) {
                    savePlace(place);
                    placesFragment.updateList();
                } else {
                    DialogCustomUtil.dialog(MainActivity.this, getString(R.string.label_title_places_exists), getString(R.string.lavel_place_exists));
                }
            }
        }
    }

    private void savePlace(Place place) {
        ContentValues val = new ContentValues();
        val.put(WakePlaceDBContract.PlacesBD.Cols.PLACE_ID, place.getId());
        val.put(WakePlaceDBContract.PlacesBD.Cols.ADDRESS, (String) place.getAddress());
        val.put(WakePlaceDBContract.PlacesBD.Cols.NAME, (String) place.getName());
        val.put(WakePlaceDBContract.PlacesBD.Cols.LATITUDE, place.getLatLng().latitude);
        val.put(WakePlaceDBContract.PlacesBD.Cols.LONGITUDE, place.getLatLng().longitude);
        contentResolver.insert(WakePlaceDBContract.PlacesBD.CONTENT_URI, val);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("Displaying permission rationale to provide additional context.");
            Snackbar.make(getMyContext(), getString(R.string.permission_rationale), getString(android.R.string.ok),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    location();

                }
            }

        }
    }


}
