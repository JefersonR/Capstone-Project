package br.wake_in_place.ui.activities;

import android.content.Context;
import android.view.View;

import br.wake_in_place.R;
import br.wake_in_place.controllers.detailImpl.DetailImpl;
import br.wake_in_place.ui.bases.BaseActivity;


public class RegisterAlarmActivity extends BaseActivity {

    @Override
    protected void setLayout(View view) {

    }

    @Override
    protected void startProperties() {
        setToolbar("Detalhes");
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

}
