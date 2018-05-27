package br.wake_in_place.ui.activities;

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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import br.wake_in_place.R;
import br.wake_in_place.controllers.mainImpl.MainImpl;
import br.wake_in_place.ui.bases.BaseActivity;
import br.wake_in_place.ui.fragments.alarm.AlarmFragment;
import br.wake_in_place.ui.fragments.places.PlacesFragment;


public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private AlarmFragment alarmFragment;
    private PlacesFragment placesFragment;



    @Override
    protected void setLayout(View view) {
        setToolbar( R.id.toolbar,false,"Wake in Place");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) fid(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) fid(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        fab = (FloatingActionButton) fid(R.id.fab);

    }

    @Override
    protected void startProperties() {
    }

    @Override
    protected void defineListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Snackbar.make(view, "Informação", "Ação", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("TESTE");*//**//*
                    }
                });*/
                startActivity(new Intent(MainActivity.this, RegisterAlarmActivity.class));
            /*    try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }*/
            }
        });

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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return alarmFragment != null ? alarmFragment : AlarmFragment.newInstance();
                case 1:
                    return placesFragment != null ? placesFragment :  PlacesFragment.newInstance();
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
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
