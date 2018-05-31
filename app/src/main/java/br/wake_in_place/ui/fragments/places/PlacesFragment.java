package br.wake_in_place.ui.fragments.places;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.wake_in_place.R;
import br.wake_in_place.controllers.mainImpl.MainImpl;
import br.wake_in_place.data.WakePlaceDBContract;
import br.wake_in_place.models.response.PlaceItem;
import br.wake_in_place.ui.adapters.PlacesAdapter;
import br.wake_in_place.ui.bases.BaseFragment;
import butterknife.BindView;

public class PlacesFragment extends BaseFragment {


    @BindView(R.id.rc_places)
    RecyclerView rcPlaces;
    @BindView(R.id.txt_nothing)
    TextView txtNothing;
    @BindView(R.id.progress)
    ProgressBar progress;
    public static final String IS_CHOICE = "is_choice";
    private ContentResolver contentResolver;
    private Cursor cursor;

    private CountDownTimer countDownTimer;

    public static PlacesFragment newInstance() {
        PlacesFragment placesFragment = new PlacesFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_CHOICE, false);
        placesFragment.setArguments(bundle);
        return placesFragment;
    }

    public static PlacesFragment newInstance(boolean isChoice) {
        PlacesFragment placesFragment = new PlacesFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_CHOICE, isChoice);
        placesFragment.setArguments(bundle);
        return placesFragment;
    }
    @Override
    public void onStop() {
        super.onStop();
        if (cursor != null) cursor.close();
    }


    @Override
    protected void startProperties() {
        contentResolver = getActivity().getContentResolver();
        rcPlaces.setAdapter(new PlacesAdapter(loadContent()));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcPlaces.setLayoutManager(llm);
    }



    @Override
    protected void defineListeners() {


    }

    private List<PlaceItem> loadContent() {
        cursor = getActivity().getContentResolver().query(WakePlaceDBContract.PlacesBD.CONTENT_URI, null, null, null, null);
        List<PlaceItem> items = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                PlaceItem placeItem = new PlaceItem(
                        "",
                        cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.PlacesBD.Cols.ADDRESS)));
                items.add(placeItem);
            } while (cursor.moveToNext());
        }
        return items;
    }





    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_places;
    }

    @Override
    protected MainImpl getControllerImpl() {
        return new MainImpl(getMyContext());
    }




}
