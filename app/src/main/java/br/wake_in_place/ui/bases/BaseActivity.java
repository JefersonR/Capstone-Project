package br.wake_in_place.ui.bases;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;

import br.wake_in_place.R;
import br.wake_in_place.utils.Constants;
import br.wake_in_place.utils.SessionManager;
import butterknife.ButterKnife;

public abstract class BaseActivity<T> extends AppCompatActivity implements Constants {


    private T controller;
    public SessionManager session;
    private Toolbar toolbar;
    private
    @IdRes
    int idToolbar = 0;
    private String titleToolbar = "";
    private int iconNav = 0;
    private boolean navigationToolbar = true;
    private boolean hasToolbar = true;
    private View.OnClickListener onClickListener;
    private ProgressBar progressBar;
    private View mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_base);
        init();
        setLayout(myView());
        startProperties();
        defineListeners();
        toolbarController(isHasToolbar());
    }

    private void toolbarController(boolean hasToolbar) {
        if (hasToolbar) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbarProperties();
        } else if (getIdToolbar() != 0) {
            findViewById(R.id.toolbar).setVisibility(View.GONE);
            toolbar = (Toolbar) getmView().findViewById(getIdToolbar());
            toolbarProperties();
        } else {
            findViewById(R.id.toolbar).setVisibility(View.GONE);
        }
    }

    private void toolbarProperties() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            if (isNavigationToolbar()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                setColorIcon();
            }
            getSupportActionBar().setTitle(getTitleToolbar());
        }
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        if (getIconNav() != 0) {
            toolbar.setNavigationIcon(getIconNav());
        }
        if (getOnClickListener() != null) {
            toolbar.setNavigationOnClickListener(getOnClickListener());
        }

    }

    private void setColorIcon() {
        Drawable backArrow = toolbar.getNavigationIcon();
        if (backArrow != null)
            backArrow.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T fid(@IdRes int id) {
        return (T) getmView().findViewById(id);
    }

    private void init() {
        setController(getControllerImpl());
        session = new SessionManager(getMyContext());
    }

    private View myView() {
        ViewStub vs = (ViewStub) findViewById(R.id.stub);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        vs.setLayoutResource(getActivityLayout());
        mView = vs.inflate();
        ButterKnife.bind(this,mView);
        return mView;
    }

    public View getmView() {
        return mView;
    }

    private void setController(T controller) {
        this.controller = controller;
    }

    protected abstract T getControllerImpl();

    public Toolbar getToolbar() {
        return toolbar;
    }

    private
    @IdRes
    int getIdToolbar() {
        return idToolbar;
    }

    public void setIdToolbar(int idToolbar) {
        this.idToolbar = idToolbar;
    }

    private String getTitleToolbar() {
        return titleToolbar;
    }

    private void setTitleToolbar(String titleToolbar) {
        this.titleToolbar = titleToolbar;
    }


    public void setToolbar(String title) {
        setTitleToolbar(title);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        };
        setOnClickListener(onClickListener);
    }

    public void setToolbar(boolean navigationToolbar, String title) {
        setTitleToolbar(title);
        setNavigationToolbar(navigationToolbar);
    }

    public void setToolbar(@IdRes int idToolbar, boolean navigationToolbar, String title) {
        setIdToolbar(idToolbar);
        setTitleToolbar(title);
        setNavigationToolbar(navigationToolbar);
        hasToolbar = false;
    }

    public void setToolbar(@IdRes int idToolbar, boolean navigationToolbar) {
        setIdToolbar(idToolbar);
        setTitleToolbar("");
        setNavigationToolbar(navigationToolbar);
    }

    public void setToolbar(@IdRes int idToolbar, String titleToolbar) {
        setIdToolbar(idToolbar);
        setTitleToolbar(titleToolbar);

    }


    public void setToolbar(String title, View.OnClickListener onClickListener) {
        setTitleToolbar(title);
        setOnClickListener(onClickListener);

    }

    public void setToolbar(String title, View.OnClickListener onClickListener, int iconNav) {
        setTitleToolbar(title);
        setOnClickListener(onClickListener);
        setIconNav(iconNav);
    }

    public void setProgress(boolean isVisible) {
        progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public boolean isHasToolbar() {
        return hasToolbar;
    }

    public void setHasToolbar(boolean hasToolbar) {
        this.hasToolbar = hasToolbar;
    }

    public int getIconNav() {
        return iconNav;
    }

    public void setIconNav(int iconNav) {
        this.iconNav = iconNav;
    }

    public void setToolbarFragments(String title) {
        setTitleToolbar(title);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                } else {
                    onBackPressed();
                }
            }
        };
        setOnClickListener(onClickListener);
    }

    public void setToolbarFragments(boolean navigationToolbar, String title) {
        setTitleToolbar(title);
        setNavigationToolbar(navigationToolbar);
    }

    public void setToolbar(int layout, String title, final boolean finish) {
        setTitleToolbar(title);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                if (finish)
                    finish();

            }
        };
        setOnClickListener(onClickListener);
    }

    private View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    private void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setToolbar() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        };
        setOnClickListener(onClickListener);
    }


    public void setToolbar(View.OnClickListener onClickListener) {
        setOnClickListener(onClickListener);

    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }


    public void setFragment(Fragment fragment, boolean stack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!stack) {
            fragmentTransaction.add(R.id.frame, fragment);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }


    public void setFragmentForResut(Fragment current_fragment, Fragment new_fragment, int requestCode) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        fragmentTransaction.replace(R.id.frame, new_fragment);
        new_fragment.setTargetFragment(current_fragment, requestCode);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public boolean isNavigationToolbar() {
        return navigationToolbar;
    }

    public void setNavigationToolbar(boolean navigationToolbar) {
        this.navigationToolbar = navigationToolbar;
    }

    protected abstract void setLayout(View view);

    protected abstract void startProperties();

    protected abstract void defineListeners();

    protected abstract
    @LayoutRes
    int getActivityLayout();

    protected abstract Context getMyContext();


}
