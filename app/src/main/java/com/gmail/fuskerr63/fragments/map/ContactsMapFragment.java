package com.gmail.fuskerr63.fragments.map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.app.ContactApplication;
import com.gmail.fuskerr63.database.User;
import com.gmail.fuskerr63.di.app.AppComponent;
import com.gmail.fuskerr63.di.map.ContactsMapComponent;
import com.gmail.fuskerr63.di.map.ContactsMapModule;
import com.gmail.fuskerr63.presenter.ContactsMapPresenter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactsMapFragment extends MvpAppCompatFragment implements ContactsMapView, OnMapReadyCallback {
    private MapView mapView;
    private ProgressBar progressBar;

    private final int PADDING = 100;

    @Inject
    Provider<ContactsMapPresenter> presenterProvider;

    @InjectPresenter
    ContactsMapPresenter contactsMapPresenter;

    private GoogleMap googleMap;

    @ProvidePresenter
    ContactsMapPresenter provideMapPresenter() {
        return presenterProvider.get();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AppComponent appComponent = ((ContactApplication) getActivity().getApplication()).getAppComponent();
        ContactsMapComponent mapComponent = appComponent.plusContactsMapComponent(new ContactsMapModule());
        mapComponent.inject(this);
    }

    @Override
    public void printMarkers(List<User> users) {
        if(googleMap != null) {
            googleMap.clear();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(User user : users) {
                LatLng postition = new LatLng(user.getLatitude(), user.getLongitude());
                builder.include(postition);
                googleMap.addMarker(new MarkerOptions().position(postition).title(user.getName()));
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, PADDING);
            googleMap.animateCamera(cameraUpdate);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ((TextView) getActivity().findViewById(R.id.title)).setText(R.string.map_title);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_map);
        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        mapView = null;
        progressBar = null;
        googleMap = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public static ContactsMapFragment newInstance() {
        return new ContactsMapFragment();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        contactsMapPresenter.onMapReady();
    }

    @Override
    public void setProgressStatus(boolean show) {
        int status = show ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(status);
    }
}

