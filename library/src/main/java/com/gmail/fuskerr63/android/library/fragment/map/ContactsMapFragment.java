package com.gmail.fuskerr63.android.library.fragment.map;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactsMapComponentContainer;
import com.gmail.fuskerr63.java.entity.ContactLocation;
import com.gmail.fuskerr63.android.library.presenter.map.ContactsMapPresenter;
import com.gmail.fuskerr63.android.library.view.ContactsMapView;
import com.gmail.fuskerr63.library.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactsMapFragment extends MvpAppCompatFragment implements
        ContactsMapView,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {
    private MapView mapView;
    private ProgressBar progressBar;

    private final int padding = 100;

    private final List<Polyline> polylines = new ArrayList<>();

    @SuppressWarnings({"WeakerAccess", "unused"})
    @Inject
    Provider<ContactsMapPresenter> presenterProvider;

    @SuppressWarnings({"WeakerAccess", "unused"})
    @InjectPresenter
    ContactsMapPresenter contactsMapPresenter;

    private GoogleMap googleMap;

    @SuppressWarnings("unused")
    @ProvidePresenter
    ContactsMapPresenter provideMapPresenter() {
        return presenterProvider.get();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Application app = Objects.requireNonNull(getActivity()).getApplication();
        if (app instanceof ContactApplicationContainer) {
            AppContainer appContainer = ((ContactApplicationContainer) app).getAppComponent();
            ContactsMapComponentContainer component = appContainer.plusContactsMapComponent();
            component.inject(this);
        }
    }

    @Override
    public void printMarkers(@Nullable List<ContactLocation> contactLocations) {
        if (googleMap != null && contactLocations != null && !contactLocations.isEmpty()) {
            googleMap.clear();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ContactLocation contactLocation : contactLocations) {
                LatLng postition = new LatLng(
                        contactLocation.getPosition().getLatitude(),
                        contactLocation.getPosition().getLongitude()
                );
                builder.include(postition);
                googleMap.addMarker(new MarkerOptions().position(postition).title(contactLocation.getName()));
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.animateCamera(cameraUpdate);
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
    public void onSaveInstanceState(@Nullable Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @NonNull
    public static ContactsMapFragment newInstance() {
        return new ContactsMapFragment();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        googleMap.setOnMarkerClickListener(this);
        contactsMapPresenter.onMapReady();
    }

    @Override
    public void setProgressStatus(boolean show) {
        int status = show ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(status);
    }

    @SuppressWarnings("unused")
    @Override
    public void clearDirection() {
        for (Polyline polyline : polylines) {
            polyline.remove();
        }
    }

    @Override
    public void showErrorToast(@Nullable String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void printDirection(@Nullable List<LatLng> points, @Nullable List<LatLng> bounds) {
        if (googleMap != null) {
            // Нарисовать линию
            final float defaultWidth = 10F;
            PolylineOptions polylineOptions = new PolylineOptions()
                    .width(defaultWidth)
                    .color((Color.BLUE))
                    .addAll(points);
            polylines.add(googleMap.addPolyline(polylineOptions));
            // Передвинуть камеру
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng bound : bounds) {
                builder.include(bound);
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), padding);
            googleMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    public boolean onMarkerClick(@Nullable Marker marker) {
        contactsMapPresenter.onMarkerClick(marker.getPosition());
        return false;
    }
}

