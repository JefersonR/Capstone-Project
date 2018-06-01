package br.wake_in_place.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import br.wake_in_place.R;
import br.wake_in_place.controllers.mainImpl.MainImpl;
import br.wake_in_place.ui.bases.BaseActivity;


public class SplashScreenActivity extends BaseActivity {
    private ImageView logo;
    private ProgressBar progressBar;
    private final int animation_time = 1000;

    protected void startProperties() {
        setHasToolbar(false);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        animation.setDuration(animation_time);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }, TIME_DELAY);
            }
        });

        logo.setAnimation(animation);
    }


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected Context getMyContext() {
        return SplashScreenActivity.this;
    }

    @Override
    protected MainImpl getControllerImpl() {
        return new MainImpl(getMyContext());
    }

    protected void setLayout(View view) {
        logo = (ImageView) fid(R.id.logo);
        progressBar = (ProgressBar) fid(R.id.progress_bar);
    }
}
