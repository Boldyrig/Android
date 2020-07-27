package com.gmail.fuskerr63.android.library.delegate.map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gmail.fuskerr63.java.entity.ContactLocation;
import com.gmail.fuskerr63.library.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class ContactsMapDelegate {
    private static final int PADDING = 100;
    private static final float DEFAULT_WIDTH = 10F;
    private final List<Polyline> polylines = new ArrayList<>();

    private ProgressBar progressBar;

    public ContactsMapDelegate(@Nullable View view) {
        if (view != null) {
            progressBar = view.findViewById(R.id.progress_bar_map);
        }
    }

    public void printMarkers(@Nullable GoogleMap googleMap, @Nullable List<ContactLocation> contactLocations) {
        if (googleMap != null && contactLocations != null && !contactLocations.isEmpty()) {
            googleMap.clear();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < contactLocations.size(); i++) {
                LatLng postition = new LatLng(
                        contactLocations.get(i).getPosition().getLatitude(),
                        contactLocations.get(i).getPosition().getLongitude()
                );
                builder.include(postition);
                googleMap.addMarker(new MarkerOptions().position(postition).title(contactLocations.get(i).getName()));
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, PADDING);
            googleMap.animateCamera(cameraUpdate);
        }
    }

    public void printDirection(
            @NonNull GoogleMap googleMap,
            @NonNull List<LatLng> points,
            @NonNull List<LatLng> bounds) {
        if (googleMap != null) {
            // Нарисовать линию
            PolylineOptions polylineOptions = new PolylineOptions()
                    .width(DEFAULT_WIDTH)
                    .color(Color.BLUE)
                    .addAll(points);
            polylines.add(googleMap.addPolyline(polylineOptions));
            // Передвинуть камеру
            if (!bounds.isEmpty()) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int i = 0; i < bounds.size(); i++) {
                    builder.include(bounds.get(i));
                }
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), PADDING);
                googleMap.animateCamera(cameraUpdate);
            }
        }
    }

    public void clearDirection() {
        for (int i = 0; i < polylines.size(); i++) {
            polylines.get(i).remove();
        }
    }

    public void showErrorToast(@Nullable String message, @Nullable Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void setProgressStatus(boolean show) {
        if (progressBar != null) {
            int status = show ? View.VISIBLE : View.GONE;
            progressBar.setVisibility(status);
        }
    }
}
