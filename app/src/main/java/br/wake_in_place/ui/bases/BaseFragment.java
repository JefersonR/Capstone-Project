package br.wake_in_place.ui.bases;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;

import br.wake_in_place.utils.Constants;
import br.wake_in_place.utils.SessionManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<T> extends Fragment implements Constants {
    private View mView;
    private T controller;
    private T Fragment;
    public SessionManager session;
    protected Context myContext;
    private Unbinder unbinder;

    @Nullable
    @Override
    @JavascriptInterface
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        try {
            mView = inflater.inflate(getFragmentLayout(), container, false);
            unbinder = ButterKnife.bind(this, mView);
        } catch (InflateException e) {
            e.getMessage();
        }

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getActivity());
        setController(getControllerImpl());
        startProperties();

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T fid(@IdRes int id) {
        return (T) getmView().findViewById(id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
    }

    public View getmView() {
        return mView;
    }

    private void setController(T controller) {
        this.controller = controller;
    }

    protected abstract void startProperties();

    protected abstract int getFragmentLayout();

    protected abstract T getControllerImpl();

    protected void setFragment(Fragment fragment) {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).setFragment(fragment, true);
    }

    protected void setFragment(Fragment fragment, boolean stack) {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).setFragment(fragment, stack);
    }

    protected void setFragmentForResult(Fragment current_fragment, Fragment new_fragment, int requestCode) {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).setFragmentForResut(current_fragment, new_fragment, requestCode);
    }

    protected ProgressBar getProgressBar() {
        return ((BaseActivity) getActivity()).getProgressBar();
    }

    protected void setProgress(boolean isVisible) {
        ((BaseActivity) getActivity()).setProgress(isVisible);
    }


    public Context getMyContext() {
        return myContext;
    }
}
