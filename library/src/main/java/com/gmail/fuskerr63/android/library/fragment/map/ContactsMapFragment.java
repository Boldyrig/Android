package com.gmail.fuskerr63.android.library.fragment.map;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.fuskerr63.android.library.delegate.map.ContactsMapDelegate;
import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactsMapComponentContainer;
import com.gmail.fuskerr63.java.entity.ContactLocation;
import com.gmail.fuskerr63.android.library.presenter.map.ContactsMapPresenter;
import com.gmail.fuskerr63.android.library.view.ContactsMapView;
import com.gmail.fuskerr63.library.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

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
    private transient MapView mapView;
    private transient ContactsMapDelegate contactsMapDelegate;
    private static final String UNUSED = "unused";

    @SuppressWarnings({"WeakerAccess", UNUSED})
    @Inject
    transient Provider<ContactsMapPresenter> presenterProvider;

    @SuppressWarnings({"WeakerAccess", UNUSED})
    @InjectPresenter
    ContactsMapPresenter contactsMapPresenter;

    private transient GoogleMap googleMap;

    @SuppressWarnings(UNUSED)
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
    public void printMarkers(@NonNull List<ContactLocation> contactLocations) {
        contactsMapDelegate.printMarkers(googleMap, contactLocations);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        contactsMapDelegate = new ContactsMapDelegate(view);
        ((TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.title)).setText(R.string.map_title);
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
        contactsMapDelegate.setProgressStatus(show);
    }

    @SuppressWarnings(UNUSED)
    @Override
    public void clearDirection() {
        contactsMapDelegate.clearDirection();
    }

    @Override
    public void showErrorToast(@Nullable String message) {
        contactsMapDelegate.showErrorToast(message, getContext());
    }

    @Override
    public void printDirection(@NonNull List<LatLng> points, @NonNull List<LatLng> bounds) {
        contactsMapDelegate.printDirection(googleMap, points, bounds);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        contactsMapPresenter.onMarkerClick(marker.getPosition());
        return false;
    }
}

