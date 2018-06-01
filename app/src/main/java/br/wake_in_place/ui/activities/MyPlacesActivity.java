package br.wake_in_place.ui.activities;

import android.content.Context;
import android.support.v4.app.Fragment;

import br.wake_in_place.R;
import br.wake_in_place.ui.bases.BaseActivityForFragment;
import br.wake_in_place.ui.fragments.places.PlacesFragment;

public class MyPlacesActivity extends BaseActivityForFragment {


    @Override
    protected void start() {
        setToolbar(getString(R.string.label_my_places));
    }

    @Override
    protected Fragment getInitFragment() {
        return PlacesFragment.newInstance(true);
    }

    @Override
    protected Object getControllerImpl() {
        return null;
    }

    @Override
    protected Context getMyContext() {
        return MyPlacesActivity.this;
    }
}
