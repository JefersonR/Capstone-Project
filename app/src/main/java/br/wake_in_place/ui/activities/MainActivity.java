package br.wake_in_place.ui.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import br.wake_in_place.R;
import br.wake_in_place.controllers.mainImpl.MainImpl;
import br.wake_in_place.data.WakePlaceContentProvider;
import br.wake_in_place.data.WakePlaceDBContract;
import br.wake_in_place.ui.bases.BaseActivity;
import br.wake_in_place.ui.fragments.alarm.AlarmFragment;
import br.wake_in_place.ui.fragments.places.PlacesFragment;
import br.wake_in_place.utils.DialogCustomUtil;
import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    private AlarmFragment alarmFragment;
    private PlacesFragment placesFragment;

    private ContentResolver contentResolver;

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private boolean isAlarmPage = true;


    @Override
    protected void startProperties() {
        setToolbar(R.id.toolbar, false, "Wake in Place");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
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

    }

    @OnClick(R.id.fab)
    public void setBtnFab(View view) {
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
                    return "Alarme";
                case 1:
                    return "Meus lugares";
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
}
