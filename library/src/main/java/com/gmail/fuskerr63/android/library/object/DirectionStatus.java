package com.gmail.fuskerr63.android.library.object;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class DirectionStatus {
    private List<LatLng> bounds;
    private List<LatLng> points;

    public DirectionStatus(List<LatLng> bounds, List<LatLng> points) {
        this.bounds = bounds;
        this.points = points;
    }

    public List<LatLng> getBounds() {
        return bounds;
    }

    public List<LatLng> getPoints() {
        return points;
    }
}
