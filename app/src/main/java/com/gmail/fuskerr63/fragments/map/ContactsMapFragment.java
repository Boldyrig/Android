package com.gmail.fuskerr63.fragments.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.app.ContactApplication;
import com.gmail.fuskerr63.database.User;
import com.gmail.fuskerr63.di.app.AppComponent;
import com.gmail.fuskerr63.di.map.MapComponent;
import com.gmail.fuskerr63.di.map.MapModule;
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

    private final int PADDING = 100;

    @Inject
    Provider<ContactsMapPresenter> presenterProvider;

    @InjectPresenter
    ContactsMapPresenter contactsMapPresenter;

    private GoogleMap googleMap;

    private final int DEFAULT_ZOOM = 15;
    private final int PERMISSIONS_REQUEST = 9090;

    @ProvidePresenter
    ContactsMapPresenter provideMapPresenter() {
        return presenterProvider.get();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AppComponent appComponent = ((ContactApplication) getActivity().getApplication()).getAppComponent();
        MapComponent mapComponent = appComponent.plusMapComponent(new MapModule(context.getApplicationContext()));
        mapComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);
        }
    }

    public void printMarkers(List<User> users) {
        if(googleMap != null) {
            googleMap.clear();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(User user : users) {

                LatLng postition = new LatLng(user.latitude, user.longitude);
                builder.include(postition);
                googleMap.addMarker(new MarkerOptions().position(postition).title(String.valueOf(user.contactId)));
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, PADDING);
            googleMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            default: {
                Toast.makeText(getActivity().getApplicationContext(), "Map closed", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ((TextView) getActivity().findViewById(R.id.title)).setText(R.string.map_title);
        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
}

