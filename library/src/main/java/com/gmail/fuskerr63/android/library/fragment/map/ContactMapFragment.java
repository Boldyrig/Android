package com.gmail.fuskerr63.android.library.fragment.map;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactMapComponentContainer;
import com.gmail.fuskerr63.android.library.presenter.map.ContactMapPresenter;
import com.gmail.fuskerr63.android.library.view.ContactMapView;
import com.gmail.fuskerr63.library.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
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

    private GoogleMap googleMap;
    private ProgressBar progressBar;

    private static final String KEY_LOCATION = "LOCATION";
    private static final int DEFAULT_ZOOM = 16;;

    @ProvidePresenter
    ContactMapPresenter provideMapPresenter() {
        return presenterProvider.get();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Application app = Objects.requireNonNull(getActivity()).getApplication();
        if (app instanceof ContactApplicationContainer) {
            AppContainer appContainer = ((ContactApplicationContainer) app).getAppComponent();
            ContactMapComponentContainer component = appContainer.plusContactMapComponent();
            component.inject(this);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ((TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.title)).setText(R.string.map_title);
        progressBar = view.findViewById(R.id.progress_bar_map);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        if (googleMap != null) {
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @NonNull
    public static ContactMapFragment newInstance(int id, @Nullable String name) {
        ContactMapFragment mapFragment = new ContactMapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        bundle.putString("NAME", name);
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        int id = Objects.requireNonNull(getArguments()).getInt("ID");
        googleMap.setOnMapClickListener(
                click -> contactMapPresenter.onMapClick(click, id, getArguments().getString("NAME"))
        );
        contactMapPresenter.showCurrentLocation(id);
    }

    @Override
    public void replaceMarker(@NonNull LatLng latLng, @Nullable String title) {
        if (googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng).title(title));
        }
    }

    @Override
    public void moveTo(@NonNull LatLng latLng) {
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        }
    }

    @Override
    public void setProgressStatus(boolean show) {
        int status = show ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(status);
    }
}
