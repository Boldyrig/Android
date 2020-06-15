package com.gmail.fuskerr63.fragments;

import android.content.pm.PackageManager;
import android.location.Location;
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
import com.gmail.fuskerr63.database.User;
import com.gmail.fuskerr63.presenter.MapPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class MapFragment extends MvpAppCompatFragment implements GoogleMapView, OnMapReadyCallback {
    private MapView mapView;

    @InjectPresenter
    MapPresenter mapPresenter;

    private Location lastKnownLocation;
    private CameraPosition cameraPosition;

    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private GoogleMap googleMap;

    List<Place.Field> placeFields = Arrays.asList(
            Place.Field.LAT_LNG);

    private final String KEY_CAMERA_POSITION = "CAMERA_POSITION";
    private final String KEY_LOCATION = "LOCATION";
    private final int DEFAULT_ZOOM = 15;
    private final int PERMISSIONS_REQUEST = 9090;

    @ProvidePresenter
    MapPresenter provideMapPresenter() {
        return new MapPresenter(getActivity().getApplicationContext(), getArguments().getInt("ID"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);
        }

        Places.initialize(getActivity().getApplicationContext(), getString(R.string.api_key));
        placesClient = Places.createClient(getContext());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
    }

    public void showCurrentLocation() {
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();
        Task<FindCurrentPlaceResponse> responseTask = placesClient.findCurrentPlace(request);
        responseTask.addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FindCurrentPlaceResponse response = task.getResult();
                if(response != null) {
                    PlaceLikelihood place = response.getPlaceLikelihoods().get(0);
                    moveTo(place.getPlace().getLatLng());
                }
            }
        }).addOnFailureListener(error -> {
            Log.d("TAG", error.getMessage());
        });
    }

    public void printMarkers(List<User> users) {
        googleMap.clear();
        for(User user : users) {
            LatLng postition = new LatLng(user.latitude, user.longitude);
            googleMap.addMarker(new MarkerOptions().position(postition).title(String.valueOf(user.contactId)));
        }
    }

    private void moveTo(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latLng.latitude,
                        latLng.longitude), DEFAULT_ZOOM));
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
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
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

    public static MapFragment newInstance(int id) {
        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if(getArguments() != null) {
            googleMap.setOnMapClickListener(click -> {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(click).title(String.valueOf(getArguments().getInt("ID"))));
                mapPresenter.onMapClick(click);
            });
        }
//        googleMap.setMapStyle(GoogleMap.MAP_TYPE_NORMAL);
//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("HELLO"));
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
