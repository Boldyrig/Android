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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmail.fuskerr63.android.library.database.User;
import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactsMapComponentContainer;
import com.gmail.fuskerr63.android.library.object.Position;
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

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactsMapFragment extends MvpAppCompatFragment implements ContactsMapView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private MapView mapView;
    private ProgressBar progressBar;
    private PolylineOptions polylineOptions;

    private final int PADDING = 100;

    List<Polyline> polylines = new ArrayList<Polyline>();

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
        Application app = getActivity().getApplication();
        if(app instanceof ContactApplicationContainer) {
            AppContainer appContainer = ((ContactApplicationContainer) app).getAppComponent();
            ContactsMapComponentContainer component = appContainer.plusContactsMapComponent();
            component.inject(this);
        }
    }

    @Override
    public void printMarkers(List<User> users) {
        if(googleMap != null && users != null && !users.isEmpty()) {
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
        googleMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        contactsMapPresenter.onMapReady();
    }

    @Override
    public void setProgressStatus(boolean show) {
        int status = show ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(status);
    }

    @Override
    public void clearDirection() {
        if(polylines != null) {
            for(Polyline polyline : polylines) {
                polyline.remove();
            }
        }
    }

    @Override
    public void showErrorToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void printDirection(List<LatLng> points, List<LatLng> bounds) {
        if(googleMap != null) {
            // Нарисовать линию
            polylineOptions = new PolylineOptions().width(10F).color((Color.BLUE)).addAll(points);
            polylines.add(googleMap.addPolyline(polylineOptions));
            // Передвинуть камеру
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(LatLng bound : bounds) {
                builder.include(bound);
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), PADDING);
            googleMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng latLng = marker.getPosition();
        contactsMapPresenter.onMarkerClick(new Position(latLng.latitude, latLng.longitude));
        return false;
    }
}

