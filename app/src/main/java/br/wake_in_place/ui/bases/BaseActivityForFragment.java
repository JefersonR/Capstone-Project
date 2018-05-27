package br.wake_in_place.ui.bases;

import android.support.v4.app.Fragment;

import br.wake_in_place.R;

/**
 * Created by Jeferson on 10/09/2017.
 */

public abstract class BaseActivityForFragment extends BaseActivity {

    @Override
    protected int getActivityLayout() {
        return R.layout.layout_fragment;
    }
    @Override
    protected void startProperties() {
        setFragment(getInitFragment(), false);
        start();
    }
    protected abstract void start();
    protected abstract Fragment getInitFragment();
}
