package com.gmail.fuskerr63.fragments.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.app.ContactApplication;
import com.gmail.fuskerr63.di.app.AppComponent;
import com.gmail.fuskerr63.di.map.ContactMapComponent;
import com.gmail.fuskerr63.di.map.ContactMapModule;
import com.gmail.fuskerr63.presenter.ContactMapPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactMapFragment extends MvpAppCompatFragment implements ContactMapView, OnMapReadyCallback {
    private MapView mapView;

    @Inject
    Provider<ContactMapPresenter> presenterProvider;

    @InjectPresenter
    ContactMapPresenter contactMapPresenter;

    private Location lastKnownLocation;
    private CameraPosition cameraPosition;

    private GoogleMap googleMap;
    private ProgressBar progressBar;

    private final String KEY_CAMERA_POSITION = "CAMERA_POSITION";
    private final String KEY_LOCATION = "LOCATION";
    private final int DEFAULT_ZOOM = 15;

    @ProvidePresenter
    ContactMapPresenter provideMapPresenter() {
        return presenterProvider.get();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AppComponent appComponent = ((ContactApplication) getActivity().getApplication()).getAppComponent();
        ContactMapComponent mapComponent = appComponent.plusContactMapComponent(
                new ContactMapModule(getArguments().getInt("ID"), getArguments().getString("NAME"))
        );
        mapComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
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
        mapView = null;
        progressBar = null;
        googleMap = null;
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
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public static ContactMapFragment newInstance(int id, String name) {
        ContactMapFragment mapFragment = new ContactMapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        bundle.putString("NAME", name);
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMapClickListener(click -> {
            contactMapPresenter.onMapClick(click);
        });
        contactMapPresenter.onMapReady();
    }

    @Override
    public void replaceMarker(LatLng latLng, String title) {
        if(googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng).title(title));
        }
    }

    @Override
    public void moveTo(LatLng latLng) {
        if(googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        }
    }

    @Override
    public void setProgressStatus(boolean show) {
        int status = show ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(status);
    }
}
