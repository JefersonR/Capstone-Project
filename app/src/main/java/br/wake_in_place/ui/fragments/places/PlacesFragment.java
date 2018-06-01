package br.wake_in_place.ui.fragments.places;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.wake_in_place.BuildConfig;
import br.wake_in_place.R;
import br.wake_in_place.controllers.mainImpl.MainImpl;
import br.wake_in_place.data.WakePlaceDBContract;
import br.wake_in_place.models.response.PlaceItem;
import br.wake_in_place.ui.adapters.PlacesAdapter;
import br.wake_in_place.ui.bases.BaseFragment;
import butterknife.BindView;

public class PlacesFragment extends BaseFragment implements PlacesAdapter.PlaceListener {


    @BindView(R.id.rc_places)
    RecyclerView rcPlaces;
    @BindView(R.id.txt_nothing)
    TextView txtNothing;
    @BindView(R.id.progress)
    ProgressBar progress;
    public static final String IS_CHOICE = "is_choice";
    private Cursor cursor;


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
        updateList();
    }


    public void updateList() {
        List<PlaceItem> items = loadContent();
        txtNothing.setVisibility((items == null || items.isEmpty()) ? View.VISIBLE : View.GONE);
        rcPlaces.setAdapter(new PlacesAdapter(items, this));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcPlaces.setLayoutManager(llm);

    }

    private List<PlaceItem> loadContent() {
        cursor = getActivity().getContentResolver().query(WakePlaceDBContract.PlacesBD.CONTENT_URI, null, null, null, null);
        List<PlaceItem> items = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                double latitude = cursor.getDouble(cursor.getColumnIndex(WakePlaceDBContract.PlacesBD.Cols.LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndex(WakePlaceDBContract.PlacesBD.Cols.LONGITUDE));
                String name = cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.PlacesBD.Cols.NAME));
                String address = cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.PlacesBD.Cols.ADDRESS));
                PlaceItem placeItem = new PlaceItem(
                        getUrl(latitude, longitude),
                        name + "\n" + address,
                        cursor.getString(cursor.getColumnIndex(WakePlaceDBContract.PlacesBD.Cols.ID)),
                        latitude,
                        longitude
                );
                items.add(placeItem);
            } while (cursor.moveToNext());
        }
        return items;
    }

    private String getUrl(double latitude, double longitude) {
        final String size = getString(R.string.str_size_parameter);
        final String key = BuildConfig.KEY;
        final int zoom = 19;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getString(R.string.str_https_parameter))
                .authority(getString(R.string.str_authority_parameter))
                .appendPath(getString(R.string.str_maps_parameter))
                .appendPath(getString(R.string.str_api_parameter))
                .appendPath(getString(R.string.str_staticmaps_parameter))
                .appendQueryParameter(getString(R.string.str_center_parameter), String.format(getString(R.string.str_lat_lng), latitude, longitude))
                .appendQueryParameter(getString(R.string.str_zoom_parameter), String.valueOf(zoom))
                .appendQueryParameter(getString(R.string.str_size_label_parameter), String.valueOf(size))
                .appendQueryParameter(getString(R.string.str_key_parameter), key);

        return builder.build().toString();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_places;
    }

    @Override
    protected MainImpl getControllerImpl() {
        return new MainImpl(getMyContext());
    }



    @Override
    public void onItemClick(View view, PlaceItem placeItem) {
        if (getArguments().getBoolean(IS_CHOICE)) {
            Intent intent = new Intent();
            intent.putExtra(IS_CHOICE, placeItem);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        } else {
            Uri gmmIntentUri = Uri.parse(String.format(getActivity().getString(R.string.str_geo), placeItem.getLatitude(), placeItem.getLongitude()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage(getString(R.string.str_google_uri));
            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                getActivity().startActivity(mapIntent);
            }
        }
    }
}
